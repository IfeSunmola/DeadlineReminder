import {Component, OnInit} from '@angular/core';
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {
	EXPIRED,
	INVALID_REQUEST_MSG,
	INVALID_RESET_LINK_MSG,
	MAX_PASSWORD_LENGTH,
	MIN_PASSWORD_LENGTH,
	PASSWORD_CHANGED_MSG
} from "../../../AppConstants";
import {MatchingPasswords} from "../../register/validator";
import {ActivatedRoute, Router} from "@angular/router";
import {AuthService} from "../../../services/http/auth.service";
import {PasswordResetData} from "../../../models/password-reset-data";
import {LoggerService} from "../../../services/http/logger.service";
import {LogBody} from "../../../models/log-body";
import {SnackbarService} from "../../../services/snackbar.service";

@Component({
	selector: 'app-confirm-reset',
	templateUrl: './confirm-reset.component.html',
	styleUrls: ['./confirm-reset.component.scss']
})
export class ConfirmResetComponent implements OnInit {
	private readonly FILE_NAME = "confirm-reset.component.ts"
	confirmResetForm!: FormGroup;
	private confirmToken = "";

	constructor(private router: Router, private activatedRoute: ActivatedRoute, private authService: AuthService,
				private logger: LoggerService, private snackbarService: SnackbarService) {
	}


	formSubmitted() {
		if (this.confirmResetForm.invalid) {
			this.confirmResetForm.markAllAsTouched()
			this.snackbarService.new("Hmm, I wonder if there's an error message showing", "")
			return;
		}

		const passwordResetData: PasswordResetData = {
			token: this.confirmToken,
			email: this.email?.value,
			password: this.password?.value,
			confirmPassword: this.confirmPassword?.value,
			message: ""
		}

		this.authService.confirmPasswordReset(passwordResetData).subscribe(
			{
				next: (response) => {
					if (response.message) { // reset success
						this.router.navigate(['/login']).then()
						this.snackbarService.new(PASSWORD_CHANGED_MSG, "OK")
					}
				},
				error: (error) => {
					const errorMessage = error.error;
					if (errorMessage.token) {
						this.logger.debug(new LogBody(this.FILE_NAME,
							"Incorrect token: ",
							`forgotPasswordData: ${JSON.stringify(passwordResetData)}\nerror: ${JSON.stringify(error)}`)
						).subscribe()
						this.router.navigate(['/']).then()
						this.snackbarService.new(INVALID_RESET_LINK_MSG, "OK")
					}
					else if (errorMessage.email) {
						this.email?.setErrors({incorrect: true});
						this.logger.debug(new LogBody(this.FILE_NAME,
							"Incorrect email: ",
							`forgotPasswordData: ${JSON.stringify(passwordResetData)}\nerror: ${JSON.stringify(error)}`)
						).subscribe()
					}
					else if (errorMessage.password || errorMessage.confirmPassword) {
						this.logger.debug(new LogBody(this.FILE_NAME,
							"Incorrect passwords: ",
							`forgotPasswordData: ${JSON.stringify(passwordResetData)}\nerror: ${JSON.stringify(error)}`)
						).subscribe()
						this.router.navigate(['/']).then()
						this.snackbarService.new(INVALID_REQUEST_MSG, "OK")
					}
					else {
						this.logger.error(new LogBody(this.FILE_NAME,
							"Unhandled Error while confirming reset password: ",
							`forgotPasswordData: ${JSON.stringify(passwordResetData)}\n error: ${JSON.stringify(error)}`)
						).subscribe()
						this.router.navigate(['/']).then()
						this.snackbarService.new(INVALID_REQUEST_MSG, "OK")
					}
				}
			}
		)
	}

	ngOnInit(): void {
		this.validateToken()
		this.confirmResetForm = new FormGroup({
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
					'',
					[
						Validators.required,
						Validators.minLength(MIN_PASSWORD_LENGTH),
						Validators.maxLength(MAX_PASSWORD_LENGTH)
					]
				),
				confirmPassword: new FormControl(
					'',
					[
						Validators.required,
						Validators.minLength(MIN_PASSWORD_LENGTH),
						Validators.maxLength(MAX_PASSWORD_LENGTH)
					]
				)
			},
			{validators: MatchingPasswords}); // For passwords
	}

	private validateToken() {
		this.activatedRoute.queryParams.subscribe(
			params => {
				const token = params['token'];
				if (!token) { // token does not exist
					this.router.navigate(['/']).then()
					this.snackbarService.new(INVALID_REQUEST_MSG, "OK")
					return;
				}
				this.confirmToken = token;
				this.authService.verifyPasswordResetCode(token).subscribe(
					(response) => {
						if (response === EXPIRED) {
							this.router.navigate(['/']).then()
							this.snackbarService.new(INVALID_RESET_LINK_MSG, "OK")
							return;
						}
					}
				)
			}
		)
	}

	get email() {
		return this.confirmResetForm.get('email');
	}

	get password() {
		return this.confirmResetForm.get('password');
	}

	get confirmPassword() {
		return this.confirmResetForm.get('confirmPassword');
	}
}
