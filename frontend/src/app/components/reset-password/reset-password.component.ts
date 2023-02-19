import {Component, OnInit} from '@angular/core';
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {Router} from "@angular/router";
import {PASSWORD_RESET} from "../../AppConstants";
import {AuthService} from "../../services/auth.service";

@Component({
	selector: 'app-reset-password',
	templateUrl: './reset-password.component.html',
	styleUrls: ['./reset-password.component.scss']
})
export class ResetPasswordComponent implements OnInit {
	resetPasswordForm!: FormGroup;

	constructor(private router: Router, private authService: AuthService) {
	}

	formSubmitted() {
		sessionStorage.setItem(PASSWORD_RESET, 'true');
		this.authService.sendPasswordResetMail(this.email?.value).subscribe(
			{
				next: () => {
					sessionStorage.setItem(PASSWORD_RESET, 'true');
					this.router.navigate(['/login'], {state: {email: this.email?.value}}).then();
				},
			}
		)
	}

	ngOnInit(): void {
		this.resetPasswordForm = new FormGroup(
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
			}
		)
	}

	get email() {
		return this.resetPasswordForm.get('email');
	}
}
