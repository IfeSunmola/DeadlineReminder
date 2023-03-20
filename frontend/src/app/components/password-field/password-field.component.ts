import {Component, Input} from '@angular/core';
import {AbstractControl, ControlContainer, FormGroup, FormGroupDirective, ValidationErrors} from "@angular/forms";

@Component({
	selector: 'app-password-field',
	templateUrl: './password-field.component.html',
	styleUrls: ['./password-field.component.scss'],
	viewProviders: [
		{
			provide: ControlContainer,
			useExisting: FormGroupDirective
		}
	]
})
export class PasswordFieldComponent {
	hide = true;
	@Input() label: string = "";
	@Input() fieldErrors: any;
	@Input() controlName: string = "";
	@Input() formGroup: FormGroup | undefined;
	@Input() isConfirmPassword: boolean = false;
}
