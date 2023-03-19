import {Component, OnInit} from '@angular/core';
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {Router} from "@angular/router";
import {AuthService} from "../../services/http/auth.service";
import {SnackbarService} from "../../services/snackbar.service";
import {PASSWORD_RESET_MSG} from "../../AppConstants";

@Component({
	selector: 'app-reset-password',
	templateUrl: './reset-password.component.html',
	styleUrls: ['./reset-password.component.scss']
})
export class ResetPasswordComponent implements OnInit {
	resetPasswordForm!: FormGroup;

	constructor(private router: Router, private authService: AuthService, private snackbarService: SnackbarService) {
	}

	formSubmitted() {
		if (this.resetPasswordForm.invalid) {
			this.snackbarService.new("I'm pretty sure you have an email", "Got it")
			return;
		}

		this.authService.sendPasswordResetMail(this.email?.value).subscribe(
			{
				next: () => {
					this.router.navigate(['/login']).then();
					const msg = PASSWORD_RESET_MSG.replace("$userEmail", this.email?.value)
					this.snackbarService.new(msg, "OK")
				},
			}
		)
	}

	ngOnInit(): void {
		this.resetPasswordForm = new FormGroup(
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
			}
		)
	}

	get email() {
		return this.resetPasswordForm.get('email');
	}
}
