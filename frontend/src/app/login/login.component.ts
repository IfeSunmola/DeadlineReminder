import {Component, OnInit} from '@angular/core';
import {AuthService} from "../services/auth.service";
import {Router} from "@angular/router";
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {LoginData} from "../models/login-data";
import {AUTH_TOKEN} from "../AppConstants";

@Component({
	selector: 'app-login',
	templateUrl: './login.component.html',
	styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {
	loginForm!: FormGroup

	constructor(private authService: AuthService, private router: Router) {
	}

	formSubmitted() {
		localStorage.removeItem(AUTH_TOKEN)
		const loginData: LoginData = {email: this.email?.value, password: this.password?.value, stayLoggedIn: this.stayLoggedIn?.value}


		this.authService.login(loginData).subscribe(
			{
				next: (response) => {
					console.log("Response: " + response)
					localStorage.setItem(AUTH_TOKEN, response)
					this.router.navigate(['/me']).then()
				},
			}
		)
	}

	ngOnInit(): void {
		this.loginForm = new FormGroup(
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
				password: new FormControl(
					"password",
					{
						validators: [
							Validators.required
						]
					}
				),
				stayLoggedIn: new FormControl(
					false
				)
			}
		)
	}

	get email() {
		return this.loginForm.get('email');
	}

	get password() {
		return this.loginForm.get('password')
	}

	get stayLoggedIn() {
		return this.loginForm.get('stayLoggedIn')
	}
}
