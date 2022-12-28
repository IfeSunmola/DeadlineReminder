import {Component} from '@angular/core';
import {LoginData} from "./login-data";

@Component({
	selector: 'app-login-form',
	templateUrl: './login-form.component.html',
	styleUrls: ['./login-form.component.scss']
})
export class LoginFormComponent {
	loginData: LoginData = new LoginData();
}
