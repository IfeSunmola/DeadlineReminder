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
	// password visibility
	passwordImg = "assets/hide.png";
	passwordVisible = false
	// confirmPassword visibility
	confirmPasswordImg = "assets/hide.png";
	confirmPasswordVisible = false;

	constructor(private router: Router, private activatedRoute: ActivatedRoute, private authService: AuthService) {
	}


	formSubmitted() {
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
					console.log("IN NEXT")
					if (response.message) { // reset success
						sessionStorage.setItem(PASSWORD_CHANGED, "true")
						this.router.navigate(['/login']).then()
					}
				},
				error: (error) => {
					const errorMessage = error.error;
					console.log("IN ERROR: " + JSON.stringify(errorMessage))
					if (errorMessage.token) {
						console.log("Reset response token: " + errorMessage.token)
						sessionStorage.setItem(INVALID_RESET_LINK, "true")
						this.router.navigate(['/']).then()
					}
					else if (errorMessage.email) {
						this.email?.setErrors({incorrect: true});
						console.log("Reset response email: " + errorMessage.email)
					}
					else if (errorMessage.password || errorMessage.confirmPassword) {
						sessionStorage.setItem(INVALID_REQUEST, "true")
						this.router.navigate(['/']).then()
						console.log("Reset response password: " + errorMessage.password)
					}
					else{
						console.log("UNHANDLED ERROR: " + JSON.stringify(errorMessage))
						sessionStorage.setItem(INVALID_REQUEST, "true")
						this.router.navigate(['/']).then()
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

	toggleConfirmPasswordVisibility() {
		if (this.confirmPasswordImg === "assets/hide.png") {
			this.confirmPasswordImg = "assets/show.png";
			this.confirmPasswordVisible = true
		}
		else {
			this.confirmPasswordImg = "assets/hide.png";
			this.confirmPasswordVisible = false
		}
	}

}
