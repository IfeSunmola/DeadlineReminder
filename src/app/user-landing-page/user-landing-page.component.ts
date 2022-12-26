import {Component, OnInit, TemplateRef} from '@angular/core';
import {NgbModal} from "@ng-bootstrap/ng-bootstrap";
import {Option} from "./Option";
import {FormBuilder} from "@angular/forms";

@Component({
	selector: 'app-user-landing-page',
	templateUrl: './user-landing-page.component.html',
	styleUrls: ['./user-landing-page.component.scss']
})
export class UserLandingPageComponent implements OnInit {
	minDate: any = {year: 0, month: 0, day: 0};

	options: Option[] = []
	title: string = "";
	dueDate: string = "";
	dueTime: string = "";


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

}

