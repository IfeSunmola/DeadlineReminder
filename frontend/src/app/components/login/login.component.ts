import {Component, OnInit} from '@angular/core';
import {AuthService} from "../../services/auth.service";
import {Router} from "@angular/router";
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {LoginData} from "../../models/login-data";
import {LoggerService} from "../../logger.service";
import {LogBody} from "../../models/log-body";
import {SnackbarService} from "../../services/snackbar.service";

@Component({
	selector: 'app-login',
	templateUrl: './login.component.html',
	styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {
	private readonly FILE_NAME = "login.component.ts"
	loginForm!: FormGroup
	hide = true;

	constructor(private authService: AuthService, private router: Router, private logger: LoggerService, private snackbarService: SnackbarService) {
	}

	formSubmitted() {
		if (this.loginForm.invalid) {
			this.snackbarService.new("How would you feel if I sent you an empty form to process?", "😔")
			return;
		}
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

	ngOnInit(): void {
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
}
