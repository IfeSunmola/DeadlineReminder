import {Component, OnInit} from '@angular/core';
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {EmailValidator, MatchingPasswords} from "./validator";
import {Router} from "@angular/router";
import {AuthService} from "../../services/auth.service";
import {INVALID_REQUEST_MSG, MAX_NICKNAME_LENGTH, MAX_PASSWORD_LENGTH, MIN_PASSWORD_LENGTH} from "../../AppConstants";
import {LoggerService} from "../../logger.service";
import {LogBody} from "../../models/log-body";
import {SnackbarService} from "../../services/snackbar.service";

@Component({
	selector: 'app-register',
	templateUrl: './register.component.html',
	styleUrls: ['./register.component.scss']
})
export class RegisterComponent implements OnInit {
	private readonly FILE_NAME = "register.component.ts"
	registerForm!: FormGroup;
	// password visibility
	passwordImg = "assets/hide.png";
	passwordVisible = false
	// confirmPassword visibility
	confirmPasswordImg = "assets/hide.png";
	confirmPasswordVisible = false;

	constructor(private router: Router, private authService: AuthService, private emailValidator: EmailValidator, private logger: LoggerService,
				private snackbarService: SnackbarService) {
	}

	ngOnInit(): void {
		//TODO: Remove default values
		this.registerForm = new FormGroup({
				nickname: new FormControl(
					'',
					{
						validators: [
							Validators.required,
							Validators.maxLength(MAX_NICKNAME_LENGTH),
						],
					}
				),

				email: new FormControl(
					'',
					{
						validators: [
							Validators.required,
							Validators.email,
						],
						asyncValidators: [
							this.emailValidator.validate.bind(this.emailValidator),
						],
						updateOn: "blur"
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
				),
				acceptedTerms: new FormControl(
					false,
					[
						Validators.requiredTrue
					]
				)
			},
			{validators: MatchingPasswords}); // For passwords
	}

	formSubmitted() {
		const registerData = this.registerForm.value;
		this.authService.registerUser(registerData)
			.subscribe(
				{
					next: (response) => {
						this.router.navigateByUrl('/register/verify',
							{state: {email: response.email, codeId: response.codeId}}).then();
					},
					error: (error) => {
						this.logger.error(new LogBody(this.FILE_NAME,
							`Got error when register form submitted.`,
							`Form: ${JSON.stringify(registerData)}. Error: ${JSON.stringify(error)}`)
						).subscribe();
						this.router.navigateByUrl('/').then();
						this.snackbarService.new(INVALID_REQUEST_MSG, "OK")
					}
				}
			);
	}

	get nickname() {
		return this.registerForm.get('nickname');
	}

	get email() {
		return this.registerForm.get('email');
	}

	get password() {
		return this.registerForm.get('password');
	}

	get confirmPassword() {
		return this.registerForm.get('confirmPassword');
	}

	get acceptedTerms() {
		return this.registerForm.get('acceptedTerms');
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