import {Component, OnInit, TemplateRef, ViewChild} from '@angular/core';
import {Option} from "../Option";
import {NgbDateStruct, NgbModal, NgbModalRef, NgbTimeStruct} from "@ng-bootstrap/ng-bootstrap";

@Component({
	selector: 'app-new-deadline',
	templateUrl: './new-deadline.component.html',
	styleUrls: ['./new-deadline.component.scss']
})
export class NewDeadlineComponent implements OnInit {
	minDate!: { year: number, month: number, day: number }; // is updated in openAddDeadlineModal

	// Form Data
	options: Option[] = []
	title!: string;
	dueDate!: NgbDateStruct;
	dueTime!: NgbTimeStruct;

	@ViewChild('modalRef') modal!: TemplateRef<any>;

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

	openModal() {
		const currentDate = new Date();
		this.minDate = {
			year: currentDate.getFullYear(),
			month: currentDate.getMonth() + 1,
			day: currentDate.getDate()
		}
		this.modalService.open(this.modal, {centered: true});
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