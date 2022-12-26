import {Component, OnInit, TemplateRef} from '@angular/core';
import {NgbDateStruct, NgbModal, NgbModalRef, NgbTimeStruct} from "@ng-bootstrap/ng-bootstrap";
import {Option} from "./Option";
import {FormBuilder} from "@angular/forms";

@Component({
	selector: 'app-user-landing-page',
	templateUrl: './user-landing-page.component.html',
	styleUrls: ['./user-landing-page.component.scss']
})
export class UserLandingPageComponent implements OnInit {
	minDate!: { year: number, month: number, day: number }; // is updated in openAddDeadlineModal

	// Form Data
	options: Option[] = []
	title!: string;
	dueDate!: NgbDateStruct;
	dueTime!: NgbTimeStruct;


	constructor(private modalService: NgbModal) {
	}

	ngOnInit(): void {
		this.options = [
			new Option("30 Minutes", true),
			new Option("1 Hour", false),
			new Option("2 Hours", false),
			new Option("3 Hours", false),
			new Option("4 Hours", false),
		]
	}

	openAddDeadlineModal(content: TemplateRef<any>) {
		const currentDate = new Date();
		this.minDate = {
			year: currentDate.getFullYear(),
			month: currentDate.getMonth() + 1,
			day: currentDate.getDate()
		}
		this.modalService.open(content, {centered: true});
	}

	onSubmit(modal: NgbModalRef, newDeadlineForm: HTMLFormElement) {
		console.log("Form has been submitted. Payload: ");
		console.log(`Title:  ${this.title}`);
		console.log(`Due Date:  ${this.dueDate.day}/${this.dueDate.month}/${this.dueDate.year}`);
		console.log(`Due Time:  ${this.dueTime.hour}:${this.dueTime.minute}`);

		newDeadlineForm.reset()
		modal.close();
	}
}

