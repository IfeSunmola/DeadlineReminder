import {Component, OnInit} from '@angular/core';
import {FormControl, FormGroup, Validators} from "@angular/forms";

@Component({
	selector: 'app-verify',
	templateUrl: './verify.component.html',
	styleUrls: ['./verify.component.scss']
})
export class VerifyComponent implements OnInit {
	verifyForm!: FormGroup;

	ngOnInit(): void {
		this.verifyForm = new FormGroup({
			code: new FormControl("",
				[
					Validators.required,
					Validators.pattern("\\d[A-Z][A-Z]\\d[A-Z]") // Digit, word, word, digit, word
				]
			),
			userEmail: new FormControl(
				"sunmolaife@gmail.com"
			)
		});
	}

	formSubmitted() {

	}

	get code() {
		return this.verifyForm.get('code');
	}
}
