import {Component} from '@angular/core';
import {RegisterData} from "./register-data";

@Component({
	selector: 'app-register-form',
	templateUrl: './register-form.component.html',
	styleUrls: ['./register-form.component.scss']
})
export class RegisterFormComponent {
	newRegister: RegisterData = new RegisterData();
}
