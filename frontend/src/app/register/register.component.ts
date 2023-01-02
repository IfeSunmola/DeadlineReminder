import {Component, OnInit} from '@angular/core';
import {AbstractControl, FormControl, FormGroup, Validators} from "@angular/forms";
import {MatchingPasswords} from "../validator";

@Component({
	selector: 'app-register',
	templateUrl: './register.component.html',
	styleUrls: ['./register.component.scss']
})
export class RegisterComponent implements OnInit {
	registerForm!: FormGroup;

	ngOnInit(): void {
		const minPassLength = 7;
		const maxPassLength = 250; // TODO: Check if db can handle encrypted password of this long
		this.registerForm = new FormGroup({
				email: new FormControl('',
					[
						Validators.required,
						Validators.email,
					]
				),
				password: new FormControl(
					'',
					[
						Validators.required,
						Validators.minLength(minPassLength),
						Validators.maxLength(maxPassLength)
					]
				),
				confirmPassword: new FormControl('',
					[
						Validators.required,
						Validators.minLength(minPassLength),
						Validators.maxLength(maxPassLength)
					]
				),
				acceptedTerms: new FormControl(false,
					[
						Validators.requiredTrue
					]
				)
			},
			{validators: MatchingPasswords}); // For passwords
	}

	formSubmitted() {
		console.log(this.registerForm.value);
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
