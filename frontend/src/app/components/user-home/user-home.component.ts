import {Component, OnInit} from '@angular/core';
import {NewDeadlineComponent} from "./new-deadline/new-deadline.component";
import {AccountInfo} from "../../models/account-info";
import {AccountService} from "../../services/account.service";
import {AuthService} from "../../services/auth.service";
import {Router} from "@angular/router";
import {NORMAL_LOGOUT, VERIFIED_SUCCESS, VERIFIED_SUCCESS_LOGIN_MESSAGE} from "../../AppConstants";
import {LogBody} from "../../models/log-body";
import {LoggerService} from "../../logger.service";

@Component({
	selector: 'app-user-home',
	templateUrl: './user-home.component.html',
	styleUrls: ['./user-home.component.scss']
})
export class UserHomeComponent implements OnInit {
	private readonly FILE_NAME = "user-home.component.ts"
	userInfo: AccountInfo | undefined;
	// verification successful, user has been logged in
	verifiedSuccess = false
	readonly VERIFIED_SUCCESS_MESSAGE = VERIFIED_SUCCESS_LOGIN_MESSAGE

	constructor(private accountService: AccountService, private authService: AuthService, private router: Router, private logger: LoggerService) {
	}

	openNewDeadlineModal(content: NewDeadlineComponent) {
		content.openModal();
	}

	ngOnInit(): void {
		this.verifiedSuccess = sessionStorage.getItem(VERIFIED_SUCCESS) === "true"
		sessionStorage.removeItem(VERIFIED_SUCCESS)

		this.accountService.getAccount().subscribe(
			{
				next: (response) => {
					this.userInfo = response
					this.logger.debug(new LogBody(this.FILE_NAME,
						"Got account info",
						`AccountInfo: ${JSON.stringify(response)}`)
					).subscribe()
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

	logoutClicked() {
		this.authService.logout();
		sessionStorage.setItem(NORMAL_LOGOUT, "true");
		this.router.navigateByUrl(`/login`).then();
	}
}
