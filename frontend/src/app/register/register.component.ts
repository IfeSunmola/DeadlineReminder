import {Component, OnInit} from '@angular/core';
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {MatchingPasswords} from "../validator";
import {Router} from "@angular/router";

@Component({
	selector: 'app-register',
	templateUrl: './register.component.html',
	styleUrls: ['./register.component.scss']
})
export class RegisterComponent implements OnInit {
	registerForm!: FormGroup;


	constructor(private router: Router) {

	}

	ngOnInit(): void {
		const minPassLength = 7;
		const maxPassLength = 250; // TODO: Check if db can handle encrypted password of this long
		//TODO: Remove default values
		this.registerForm = new FormGroup({
				email: new FormControl('sunmolaife@gmail.com',
					[
						Validators.required,
						Validators.email,
					]
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
		console.log(this.registerForm.value);
		this.router.navigate(['/register/verify']).then();
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
