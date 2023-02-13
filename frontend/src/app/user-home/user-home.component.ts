import {Component, OnInit} from '@angular/core';
import {NewDeadlineComponent} from "./new-deadline/new-deadline.component";
import {AccountInfo} from "../models/account-info";
import {AccountService} from "../services/account.service";

@Component({
	selector: 'app-user-home',
	templateUrl: './user-home.component.html',
	styleUrls: ['./user-home.component.scss']
})
export class UserHomeComponent implements OnInit {
	userInfo: AccountInfo | undefined;

	constructor(private accountService: AccountService) {
	}

	openNewDeadlineModal(content: NewDeadlineComponent) {
		content.openModal();
	}

	ngOnInit(): void {
		this.accountService.getAccount().subscribe(
			{
				next: (response) => {
					this.userInfo = response
					console.log("Response: " + JSON.stringify(this.userInfo))
				},
			}
		)
	}

	get email() {
		return this.userInfo?.email
	}

	get nickname() {
		return this.userInfo?.nickname
	}
}
