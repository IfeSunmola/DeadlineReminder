import {Component, OnInit} from '@angular/core';
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {EmailValidator, MatchingPasswords} from "./validator";
import {Router} from "@angular/router";
import {AuthService} from "../../services/auth.service";
import {MAX_NICKNAME_LENGTH, MAX_PASSWORD_LENGTH, MIN_PASSWORD_LENGTH} from "../../AppConstants";

@Component({
	selector: 'app-register',
	templateUrl: './register.component.html',
	styleUrls: ['./register.component.scss']
})
export class RegisterComponent implements OnInit {
	registerForm!: FormGroup;
	invalidRequest: boolean = false;

	constructor(private router: Router, private authService: AuthService, private emailValidator: EmailValidator) {
	}

	ngOnInit(): void {
		// show any error messages, clear after so it disappears on refresh
		this.invalidRequest = sessionStorage.getItem('invalidRequest') === 'true';
		sessionStorage.removeItem('invalidRequest');

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
						// const obj = JSON.parse(JSON.stringify(error));
						// console.log(obj.error);
						this.router.navigateByUrl('/').then();
						console.log("What did you do? SMH.");
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
}
