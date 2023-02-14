import {Component, OnInit} from '@angular/core';
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {AuthService} from "../../services/auth.service";
import {Router} from "@angular/router";
import {VerifyCodeData} from "../../models/verify-code-data";
import {DISABLED_ACCOUNT, DISABLED_ACCOUNT_MESSAGE, EXPIRED, INCORRECT, INVALID_REQUEST, SUCCESS, VERIFY_CODE_LENGTH} from "../../AppConstants";

@Component({
	selector: 'app-verify',
	templateUrl: './verify.component.html',
	styleUrls: ['./verify.component.scss']
})
export class VerifyComponent implements OnInit {
	userEmail: string = "";
	codeId: number = -1;
	verifyForm!: FormGroup;
	// user is disabled
	disabledAccount: boolean = false;
	readonly DISABLED_ACCOUNT_MESSAGE = DISABLED_ACCOUNT_MESSAGE

	constructor(private router: Router, private authService: AuthService) {
		this.userEmail = this.router.getCurrentNavigation()?.extras.state?.['email'];
		this.codeId = this.router.getCurrentNavigation()?.extras.state?.['codeId'];
		// if this.userEmail is null, then the user is trying to access this page directly
		// and should be redirected to the register page
		if (this.userEmail == null || this.codeId == null) {
			this.router.navigateByUrl('/register').then();
		}
	}

	ngOnInit(): void {
		this.disabledAccount = localStorage.getItem(DISABLED_ACCOUNT) === "true";
		localStorage.removeItem(DISABLED_ACCOUNT);

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
		const verifyCodeData: VerifyCodeData = {codeId: this.codeId, userEmail: this.userEmail, codeFromUser: this.code?.value};
		if (typeof this.codeId == undefined || !this.codeId || typeof this.userEmail == undefined || !this.userEmail) {
			// code id wasn't gotten for some reason or the user email is not in the email field
			// shouldn't execute if nothing fishy is going on
			this.router.navigateByUrl('/register').then();
		}

		this.authService.verifyCode(verifyCodeData).subscribe(
			{
				next: (response) => {
					if (response === SUCCESS) {
						this.router.navigateByUrl('/login').then();
					}
					else if (response === INCORRECT) {
						this.code?.setErrors({incorrect: true});
						console.log("Incorrect code");
					}
					else if (response === EXPIRED) {
						this.code?.setErrors({expired: true});
					}
					else {
						localStorage.setItem(INVALID_REQUEST, "true");
						this.router.navigateByUrl('/').then();
						console.log("Something funny happened: " + response);
					}
				},
			}
		)
	}

	get code() {
		return this.verifyForm.get('code');
	}
}
