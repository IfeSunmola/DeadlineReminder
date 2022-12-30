import {Component, OnInit} from '@angular/core';
import {AuthService} from "../../services/auth.service";
import {FormControl, FormGroup, Validators} from "@angular/forms";

@Component({
	selector: 'app-login-form',
	templateUrl: './login-form.component.html',
	styleUrls: ['./login-form.component.scss']
})
export class LoginFormComponent implements OnInit {
	loginForm!: FormGroup;

	constructor(private authService: AuthService) {

	}

	ngOnInit(): void {
		this.loginForm = new FormGroup({
			email: new FormControl('sunmolaife@gmail.com', [Validators.required, Validators.email]),
			password: new FormControl('1234567', [Validators.required, Validators.minLength(7)]),
			stayLoggedIn: new FormControl(false)
		});
	}

	doLogin() {
		console.log("Form Group: ", this.loginForm.value);
		if (this.loginForm.valid) {
			this.authService.login(this.loginForm).subscribe(
				result => {
					if (result.token !== null) {
						console.log("Result: " + result)
					}
				}
			);
		}

	}
}
