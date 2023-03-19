import {Component, OnInit} from '@angular/core';
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {AuthService} from "../../../services/http/auth.service";
import {Router} from "@angular/router";
import {VerifyCodeData} from "../../../models/verify-code-data";
import {
	EXPIRED,
	INCORRECT,
	INVALID_REQUEST_MSG,
	SUCCESS,
	VERIFIED_SUCCESS_LOGIN_MSG,
	VERIFY_CODE_LENGTH,
	VERIFY_CODE_SENT_SUCCESS,
} from "../../../AppConstants";
import {LoggerService} from "../../../services/http/logger.service";
import {LogBody} from "../../../models/log-body";
import {SnackbarService} from "../../../services/snackbar.service";

@Component({
	selector: 'app-verify',
	templateUrl: './verify.component.html',
	styleUrls: ['./verify.component.scss']
})
export class VerifyComponent implements OnInit {
	private readonly FILE_NAME = "verify.component.ts"
	userEmail: string = "";
	codeId: number = -1;
	verifyForm!: FormGroup;

	constructor(private router: Router, private authService: AuthService, private logger: LoggerService, private snackbarService: SnackbarService) {
		this.userEmail = this.router.getCurrentNavigation()?.extras.state?.['email'];
		this.codeId = this.router.getCurrentNavigation()?.extras.state?.['codeId'];
		// if this.userEmail is null, then the user is trying to access this page directly
		// and should be redirected to the register page
		if (this.userEmail == null || this.codeId == null) {
			this.logger.debug(new LogBody(this.FILE_NAME, "User is trying to access this page directly")).subscribe();
			this.router.navigateByUrl('/register').then();
		}
	}

	ngOnInit(): void {
		this.verifyForm = new FormGroup({
			userEmail: new FormControl(
				this.userEmail
			),
			code: new FormControl(
				"",
				{
					validators: [
						Validators.required,
						Validators.minLength(VERIFY_CODE_LENGTH),
						Validators.maxLength(VERIFY_CODE_LENGTH)
					]
				}
			),
		});
	}

	formSubmitted() {
		if (this.verifyForm.invalid) {
			this.snackbarService.new("I can see some errors", "")
			return;
		}
		const verifyCodeData: VerifyCodeData = {codeId: this.codeId, userEmail: this.userEmail, codeFromUser: this.code?.value};
		if (typeof this.codeId == undefined || !this.codeId || typeof this.userEmail == undefined || !this.userEmail) {
			// code id wasn't gotten for some reason or the user email is not in the email field
			// shouldn't execute if nothing fishy is going on
			this.logger.error(new LogBody(
				this.FILE_NAME,
				`codeId or userEmail is null or undefined`,
				`VerifyCodeData: ${JSON.stringify(verifyCodeData)}`
			)).subscribe();
			this.router.navigateByUrl('/register').then();
		}

		this.authService.verifyCode(verifyCodeData).subscribe(
			{
				next: (response) => {
					const message = response.message;
					if (message === SUCCESS) { // if success, then token was included
						this.authService.setAuthToken(response.token);
						this.router.navigateByUrl('/me').then();
						this.snackbarService.new(VERIFIED_SUCCESS_LOGIN_MSG, "OK")
					}
					else if (message === INCORRECT) {
						this.code?.setErrors({incorrect: true});
					}
					else if (message === EXPIRED) {
						this.code?.setErrors({expired: true});
					}
					else {
						this.logger.error(new LogBody(
							this.FILE_NAME,
							`Unhandled response from server`,
							`Message: ${message}, Response: ${JSON.stringify(response)}`
						)).subscribe();
						this.router.navigateByUrl('/').then();
						this.snackbarService.new(INVALID_REQUEST_MSG, "OK")
					}
				},
			}
		)
	}

	get code() {
		return this.verifyForm.get('code');
	}

	resendButtonClicked() {
		this.authService.sendVerificationCode(this.userEmail).subscribe(
			{
				next: (response) => {
					if (response.email !== this.userEmail) { // random safeguard
						this.logger.debug(new LogBody(
							this.FILE_NAME,
							`Email mismatch between response and user email`,
							`Response: ${JSON.stringify(response)}`
						)).subscribe();
						this.router.navigate(['/login']).then()
					}
					else {
						// page refresh
						this.router.navigateByUrl('/', {skipLocationChange: true}).then(() =>
							this.router.navigate(["/register/verify"], {state: {email: this.userEmail, codeId: response.codeId}}).then())
						this.snackbarService.new(VERIFY_CODE_SENT_SUCCESS, "OK")
					}
				}
			}
		)
	}
}
