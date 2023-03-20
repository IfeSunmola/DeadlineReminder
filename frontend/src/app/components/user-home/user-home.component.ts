import {Component, OnInit} from '@angular/core';
import {AccountInfo} from "../../models/account-info";
import {AccountService} from "../../services/http/account.service";
import {AuthService} from "../../services/http/auth.service";
import {Router} from "@angular/router";
import {LoggerService} from "../../services/http/logger.service";
import {SnackbarService} from "../../services/snackbar.service";
import {NORMAL_LOGOUT_MSG} from "../../AppConstants";
import {MatDialog} from "@angular/material/dialog";
import {NewDeadlineComponent} from "./new-deadline/new-deadline.component";
import {DeadlineService} from "../../services/http/deadline.service";
import {Observable} from "rxjs";
import {Deadline} from "../../models/deadline";

@Component({
	selector: 'app-user-home',
	templateUrl: './user-home.component.html',
	styleUrls: ['./user-home.component.scss']
})
export class UserHomeComponent implements OnInit {
	private readonly FILE_NAME = "user-home.component.ts"
	userInfo: AccountInfo | undefined;
	userDeadlines: Observable<Deadline[]> | undefined;

	constructor(private accountService: AccountService, private authService: AuthService, private router: Router,
				private logger: LoggerService, private snackbarService: SnackbarService, private dialog: MatDialog,
				public deadlineService: DeadlineService
	) {
	}


	ngOnInit(): void {
		this.accountService.getAccount().subscribe(
			{
				next: (response) => {
					this.userInfo = response
					// got user info, now load deadlines
					this.userDeadlines = this.deadlineService.getDeadlines(this.userInfo?.email)
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

	openNewDeadline() {
		const dialogRef = this.dialog.open(
			NewDeadlineComponent, {
				width: '25rem',
				autoFocus: false,
				data: this.userInfo?.email
			}
		);

		dialogRef.afterClosed().subscribe(result => {
			console.log(`Dialog result: ${JSON.stringify(result)}`);
		});
	}
}
