import {Component, OnInit} from '@angular/core';
import {Option} from "../Option";
import {MatDialogRef} from "@angular/material/dialog";
import {AbstractControl, FormControl, FormGroup, ValidationErrors, ValidatorFn, Validators} from "@angular/forms";
import {TimesService} from "../../../services/http/times.service";
import {Observable} from "rxjs";

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
	times: Observable<{ name: string }[]> | undefined;

	constructor(private dialogRef: MatDialogRef<NewDeadlineComponent>, private timesService: TimesService) {
	}

	ngOnInit(): void {
		const today = new Date()
		const currentMonth = today.getMonth()
		const currentDay = today.getDate()
		// minimum date is the next day, maximum date is 2 months from now
		this.minDate = new Date(today.getFullYear(), currentMonth, currentDay + 1)
		this.maxDate = new Date(today.getFullYear(), currentMonth + 2, currentDay)

		// save the observable, will be used in html. For 7:00 PM, 8:00 PM, etc.
		this.times = this.timesService.dueTimes

		// for the reminder times, e.g. 30 Minutes, 1 Hour, 2 Hours, etc.
		this.timesService.reminderTimes.subscribe(times => {
			this.timeOptions = times.map(time => new Option(time.name, time.checkedByDefault)) // used in html to get all the options
			this.formData.patchValue( // used get the pre-selected options
				{
					options: this.timeOptions.filter(option => option.isChecked).map(
						option => option.name
					)
				}
			)
		})

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
					[], // the pre-selected options will be updated when the timeOptions are loaded above
					{
						validators: [
							Validators.required,
							this.optionsValidator() // make sure at least one option is selected
						],
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
			console.log(control.value)
			return control.value.length > 0 ? null : {optionsNotSelected: true};
		};
	}
}