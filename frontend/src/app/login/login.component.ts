import {Component, OnInit} from '@angular/core';
import {AuthService} from "../services/auth.service";
import {Router} from "@angular/router";
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {LoginData} from "../models/login-data";
import {
	AUTH_TOKEN,
	EXPIRED_SESSION,
	EXPIRED_SESSION_MESSAGE,
	LOGIN_NEEDED,
	LOGIN_NEEDED_MESSAGE,
	NORMAL_LOGOUT,
	NORMAL_LOGOUT_MESSAGE
} from "../AppConstants";

@Component({
	selector: 'app-login',
	templateUrl: './login.component.html',
	styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {
	loginForm!: FormGroup
	// user logged out normally
	normalLogout: boolean = false;
	readonly NORMAL_LOGOUT_MESSAGE = NORMAL_LOGOUT_MESSAGE
	// jwt token has expired, from AuthInterceptor
	expiredSession: boolean = false;
	readonly EXPIRED_SESSION_MESSAGE = EXPIRED_SESSION_MESSAGE
	// user is not logged in, tries to access a protected route, from AuthGuard
	loginNeeded: boolean = false;
	readonly LOGIN_NEEDED_MESSAGE = LOGIN_NEEDED_MESSAGE


	constructor(private authService: AuthService, private router: Router) {
	}

	formSubmitted() {
		localStorage.removeItem(AUTH_TOKEN)
		const loginData: LoginData = {email: this.email?.value, password: this.password?.value, stayLoggedIn: this.stayLoggedIn?.value}

		this.authService.login(loginData).subscribe(
			{
				next: (response) => { // invalid login will be caught by AuthInterceptor
					console.log("Response: " + JSON.stringify(response))
					localStorage.setItem(AUTH_TOKEN, response.token)
					this.router.navigate(['/me']).then()
				},
			}
		)
	}

	ngOnInit(): void {
		this.normalLogout = localStorage.getItem(NORMAL_LOGOUT) === "true"
		localStorage.removeItem(NORMAL_LOGOUT)

		this.expiredSession = localStorage.getItem(EXPIRED_SESSION) === "true"
		localStorage.removeItem(EXPIRED_SESSION)

		this.loginNeeded = localStorage.getItem(LOGIN_NEEDED) === "true"
		localStorage.removeItem(LOGIN_NEEDED)

		this.loginForm = new FormGroup(
			{
				email: new FormControl(
					"sunmolaife@gmail.com",
					{
						validators: [
							Validators.required,
							Validators.email
						]
					}
				),
				password: new FormControl(
					"password",
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
}
