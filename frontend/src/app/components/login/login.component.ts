import {Component, OnInit} from '@angular/core';
import {AuthService} from "../../services/auth.service";
import {Router} from "@angular/router";
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {LoginData} from "../../models/login-data";
import {
	INVALID_CREDENTIALS,
	INVALID_SESSION,
	INVALID_SESSION_MESSAGE,
	LOGIN_NEEDED,
	LOGIN_NEEDED_MESSAGE,
	NORMAL_LOGOUT,
	NORMAL_LOGOUT_MESSAGE,
	PASSWORD_CHANGED,
	PASSWORD_CHANGED_MESSAGE,
	PASSWORD_RESET,
	PASSWORD_RESET_MESSAGE,
} from "../../AppConstants";
import {LoggerService} from "../../logger.service";
import {LogBody} from "../../models/log-body";

@Component({
	selector: 'app-login',
	templateUrl: './login.component.html',
	styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {
	private readonly FILE_NAME = "login.component.ts"
	loginForm!: FormGroup
	//password visibility
	passwordImg = "assets/hide.png";
	passwordVisible = false
	// user logged out normally
	normalLogout = false;
	readonly NORMAL_LOGOUT_MESSAGE = NORMAL_LOGOUT_MESSAGE
	// user is not logged in, tries to access a protected route, from AuthGuard
	loginNeeded = false;
	readonly LOGIN_NEEDED_MESSAGE = LOGIN_NEEDED_MESSAGE
	// user was already logged in but the jwt became invalid
	invalidSession = false;
	readonly INVALID_SESSION_MESSAGE = INVALID_SESSION_MESSAGE
	// invalid username or password
	invalidCredentials: boolean = false;
	readonly INVALID_CREDENTIALS_MESSAGE = INVALID_CREDENTIALS
	// password reset
	passwordReset = true
	readonly PASSWORD_RESET_MESSAGE = PASSWORD_RESET_MESSAGE
	passwordResetEmail = ""
	// password reset successful
	passwordChanged = false
	readonly PASSWORD_CHANGED_MESSAGE = PASSWORD_CHANGED_MESSAGE

	constructor(private authService: AuthService, private router: Router, private logger: LoggerService) {
		this.passwordResetEmail = this.router.getCurrentNavigation()?.extras.state?.['email'];
	}

	formSubmitted() {
		this.authService.deleteAuthToken()
		const loginData: LoginData = {email: this.email?.value, password: this.password?.value, stayLoggedIn: this.stayLoggedIn?.value}

		this.authService.login(loginData).subscribe(
			{
				next: (response) => { // invalid login will be caught by AuthInterceptor
					this.logger.debug(new LogBody(this.FILE_NAME,
						"Login Success: ",
						`Response: ${JSON.stringify(response)}`)
					).subscribe()
					this.authService.setAuthToken(response.token)
					this.router.navigate(['/me']).then()
				},
			}
		)
	}

	private initMessages() {
		this.normalLogout = sessionStorage.getItem(NORMAL_LOGOUT) === "true"
		sessionStorage.removeItem(NORMAL_LOGOUT)

		this.loginNeeded = sessionStorage.getItem(LOGIN_NEEDED) === "true"
		sessionStorage.removeItem(LOGIN_NEEDED)

		this.invalidSession = sessionStorage.getItem(INVALID_SESSION) === "true"
		sessionStorage.removeItem(INVALID_SESSION)

		this.invalidCredentials = sessionStorage.getItem(INVALID_CREDENTIALS) === "true"
		sessionStorage.removeItem(INVALID_CREDENTIALS)

		this.passwordReset = sessionStorage.getItem(PASSWORD_RESET) === "true"
		sessionStorage.removeItem(PASSWORD_RESET)

		this.passwordChanged = sessionStorage.getItem(PASSWORD_CHANGED) === "true"
		sessionStorage.removeItem(PASSWORD_CHANGED)
	}

	ngOnInit(): void {
		this.initMessages()

		this.loginForm = new FormGroup(
			{
				email: new FormControl(
					"",
					{
						validators: [
							Validators.required,
							Validators.email
						]
					}
				),
				password: new FormControl(
					"",
					{
						validators: [
							Validators.required
						]
					}
				),
				stayLoggedIn: new FormControl(
					false
				)
			}
		)
	}

	get email() {
		return this.loginForm.get('email');
	}

	get password() {
		return this.loginForm.get('password')
	}

	get stayLoggedIn() {
		return this.loginForm.get('stayLoggedIn')
	}

	resetPasswordClicked() {
		this.router.navigate(['/reset-password']).then()
	}

	togglePasswordVisibility() {
		if (this.passwordImg === "assets/hide.png") {
			this.passwordImg = "assets/show.png";
			this.passwordVisible = true
		}
		else {
			this.passwordImg = "assets/hide.png";
			this.passwordVisible = false
		}
	}
}
