import {Component, OnInit} from '@angular/core';
import {NewDeadlineComponent} from "./new-deadline/new-deadline.component";
import {AccountInfo} from "../../models/account-info";
import {AccountService} from "../../services/account.service";
import {AuthService} from "../../services/auth.service";
import {Router} from "@angular/router";
import {LogBody} from "../../models/log-body";
import {LoggerService} from "../../services/logger.service";
import {SnackbarService} from "../../services/snackbar.service";
import {NORMAL_LOGOUT_MSG} from "../../AppConstants";

@Component({
	selector: 'app-user-home',
	templateUrl: './user-home.component.html',
	styleUrls: ['./user-home.component.scss']
})
export class UserHomeComponent implements OnInit {
	private readonly FILE_NAME = "user-home.component.ts"
	userInfo: AccountInfo | undefined;

	constructor(private accountService: AccountService, private authService: AuthService, private router: Router, private logger: LoggerService,
				private snackbarService: SnackbarService) {
	}

	openNewDeadlineModal(content: NewDeadlineComponent) {
		content.openModal();
	}

	ngOnInit(): void {
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
		this.router.navigateByUrl(`/login`).then();
		this.snackbarService.new(NORMAL_LOGOUT_MSG, "OK")
	}
}
