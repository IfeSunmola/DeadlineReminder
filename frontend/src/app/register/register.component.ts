import {Component, OnInit} from '@angular/core';
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {EmailValidator, MatchingPasswords} from "./validator";
import {Router} from "@angular/router";
import {AuthService} from "../services/auth.service";

@Component({
	selector: 'app-register',
	templateUrl: './register.component.html',
	styleUrls: ['./register.component.scss']
})
export class RegisterComponent implements OnInit {
	registerForm!: FormGroup;
	hasErrors: boolean = false;
	serverErrorMsg: string = "";
	emailExistsMsg: string = "";


	constructor(private router: Router, private authService: AuthService, private emailValidator: EmailValidator) {
	}


	ngOnInit(): void {
		const minPassLength = 7;
		const maxPassLength = 250; // TODO: Check if db can handle encrypted password of this long
		//TODO: Remove default values
		this.registerForm = new FormGroup({
				email: new FormControl(
					'sunmolaife@gmail.com',
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
					'12345678',
					[
						Validators.required,
						Validators.minLength(minPassLength),
						Validators.maxLength(maxPassLength)
					]
				),
				confirmPassword: new FormControl('12345678',
					[
						Validators.required,
						Validators.minLength(minPassLength),
						Validators.maxLength(maxPassLength)
					]
				),
				acceptedTerms: new FormControl(
					true,
					[
						Validators.requiredTrue
					]
				)
			},
			{validators: MatchingPasswords}); // For passwords
	}

	formSubmitted() {
		this.authService.registerUser(this.registerForm.value)
			.subscribe(
				{
					next: (response) => {
						console.log("In next: " + JSON.stringify(response))
						console.log("In next: " + response)
						this.router.navigate(['/register/verify']).then();
					},
					error: (error) => {
						this.hasErrors = true;
						this.serverErrorMsg = error;
						this.emailExistsMsg = error.error.email;
						console.log("Email exists msg " + this.emailExistsMsg);
					},
					complete: () => {
						console.log("In complete");
					}
				}
			);
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
