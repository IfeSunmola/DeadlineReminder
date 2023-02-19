import {Component, OnInit} from '@angular/core';
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {EXPIRED, INVALID_REQUEST, INVALID_RESET_LINK, MAX_PASSWORD_LENGTH, MIN_PASSWORD_LENGTH, PASSWORD_CHANGED} from "../../../AppConstants";
import {MatchingPasswords} from "../../register/validator";
import {ActivatedRoute, Router} from "@angular/router";
import {AuthService} from "../../../services/auth.service";
import {PasswordResetData} from "../../../models/password-reset-data";

@Component({
	selector: 'app-confirm-reset',
	templateUrl: './confirm-reset.component.html',
	styleUrls: ['./confirm-reset.component.scss']
})
export class ConfirmResetComponent implements OnInit {
	confirmResetForm!: FormGroup;
	private confirmToken = "";

	constructor(private router: Router, private activatedRoute: ActivatedRoute, private authService: AuthService) {
	}


	formSubmitted() {
		const passwordResetData: PasswordResetData = {
			token: this.confirmToken,
			email: this.email?.value,
			password: this.password?.value,
			confirmPassword: this.confirmPassword?.value
		}
		this.authService.confirmPasswordReset(passwordResetData).subscribe(
			(response) => {
				if (response.token) {
					sessionStorage.setItem(INVALID_RESET_LINK, "true")
					this.router.navigate(['/']).then()
				}
				else if (response.email) {
					this.email?.setErrors({incorrect: true});
					console.log("Reset response email: " + response.email)
				}
				else if (response.password || response.confirmPassword) {
					sessionStorage.setItem(INVALID_REQUEST, "true")
					this.router.navigate(['/']).then()
					console.log("Reset response password: " + response.password)
				}
				else { // reset success
					sessionStorage.setItem(PASSWORD_CHANGED, "true")
					this.router.navigate(['/login']).then()
				}

			}
		)
	}

	ngOnInit(): void {
		this.validateToken()
		this.confirmResetForm = new FormGroup({
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
					'dwerrerwwrewewedfdf',
					[
						Validators.required,
						Validators.minLength(MIN_PASSWORD_LENGTH),
						Validators.maxLength(MAX_PASSWORD_LENGTH)
					]
				),
				confirmPassword: new FormControl(
					'dwerrerwwrewewedfdf',
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
					sessionStorage.setItem(INVALID_REQUEST, "true")
					this.router.navigate(['/']).then()
					return;
				}
				this.confirmToken = token;
				console.log("Token: " + token)
				this.authService.verifyPasswordResetCode(token).subscribe(
					(response) => {
						if (response === EXPIRED) {
							sessionStorage.setItem(INVALID_RESET_LINK, "true")
							this.router.navigate(['/']).then()
						}
						console.log("Email: " + response)
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
