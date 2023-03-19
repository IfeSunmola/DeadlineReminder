import {Component, OnInit} from '@angular/core';
import {Option} from "../Option";
import {MatDialogRef} from "@angular/material/dialog";
import {AbstractControl, FormControl, FormGroup, ValidationErrors, ValidatorFn, Validators} from "@angular/forms";

@Component({
	selector: 'app-new-deadline',
	templateUrl: './new-deadline.component.html',
	styleUrls: ['./new-deadline.component.scss']
})
export class NewDeadlineComponent implements OnInit {
	minDate: Date | undefined;
	maxDate: Date | undefined
	// Form Data
	formData!: FormGroup
	timeOptions: Option[] = [] // e.g. 30 Minutes, 1 Hour, 2 Hours, etc.
	times: string[] = []; // e.g. 7:00 AM, 7:30 AM, 8:00 AM, etc.

	constructor(private dialogRef: MatDialogRef<NewDeadlineComponent>) {
	}

	ngOnInit(): void {
		const today = new Date()
		const currentMonth = today.getMonth()
		const currentDay = today.getDate()
		// minimum date is the next day, maximum date is 2 months from now
		this.minDate = new Date(today.getFullYear(), currentMonth, currentDay + 1)
		this.maxDate = new Date(today.getFullYear(), currentMonth + 2, currentDay)


		this.times = ["7:00 AM", "7:30 AM", "8:00 AM", "8:30 AM", "9:00 AM", "9:30 AM", "10:00 AM", "10:30 AM",
			"11:00 AM", "11:30 AM", "12:00 PM (Noon)", "12:30 PM", "1:00 PM", "1:30 PM", "2:00 PM", "2:30 PM", "3:00 PM", "3:30 PM",
			"4:00 PM", "4:30 PM", "5:00 PM", "5:30 PM", "6:00 PM", "6:30 PM", "7:00 PM", "7:30 PM", "8:00 PM", "8:30 PM", "9:00 PM",
			"9:30 PM", "10:00 PM", "10:30 PM", "11:00 PM", "11:30 PM", "12:00 AM (Midnight)"]

		this.timeOptions = [
			new Option("30 Minutes", true),
			new Option("1 Hour", true),
			new Option("2 Hours", false),
			new Option("3 Hours", false),
			new Option("4 Hours", false),
		]

		this.formData = new FormGroup(
			{
				title: new FormControl(
					"",
					{
						validators: [
							Validators.required,
							Validators.minLength(4),
							Validators.maxLength(25)
						]
					}
				),
				dueDate: new FormControl(
					"",
					{
						validators: [
							Validators.required
						]
					}
				),
				dueTime: new FormControl(
					"",
					{
						validators: [
							Validators.required
						]
					}
				),
				options: new FormControl(
					this.timeOptions.filter(option => option.isChecked).map(
						option => option.name
					),
					{
						validators: [
							Validators.required,
							this.optionsValidator() // make sure at least one option is selected
						]
					},
				),
				otherPeopleSee: new FormControl(
					false,
				)
			}
		)
	}

	get title() {
		return this.formData.get('title');
	}

	get dueDate() {
		return this.formData.get('dueDate');
	}

	get dueTime() {
		return this.formData.get('dueTime');
	}

	get options() {
		return this.formData.get('options');
	}

	cancelClicked() {
		this.dialogRef.close()
		console.log("Cancel clicked")
	}

	saveClicked() {
		if (this.formData.invalid) {
			this.formData.markAllAsTouched()
			return;
		}
		this.dialogRef.close(this.formData.value)
		console.log("Save clicked")
	}

	optionsValidator(): ValidatorFn {
		return (control: AbstractControl): ValidationErrors | null => {
			return control.value.length > 0 ? null : {optionsNotSelected: true};
		};
	}

}