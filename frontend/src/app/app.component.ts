import {Component, HostListener, OnInit} from '@angular/core';
import {SnackbarService} from "./services/snackbar.service";
import {timer} from "rxjs";

@Component({
	selector: 'app-root',
	templateUrl: './app.component.html',
	styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit {
	smallScreen = false;

	constructor(private snackbarService: SnackbarService) {
	}

	ngOnInit(): void {
		this.smallScreen = window.innerWidth < 768;
		const messages =
			[
				"1. This app is still under development.",
				"2. Only user authentication has been implemented.",
				"3. This message will be shown regularly.",
				`4. Repo: 
					<a target='_blank' href='https://github.com/IfeSunmola/DeadlineReminder'>
						https://github.com/IfeSunmola/DeadlineReminder
					</a>`
			]
		const snackbarData = {
			title: "Note: March 19, 2023",
			messages: messages,
			action: "GOT IT"
		}
		timer(0, 600000).subscribe(
			() => {
				this.snackbarService.newFromComponent(snackbarData, 20000)
			}
		)

	}

	@HostListener('window:resize', ['$event'])
	onWindowResize() {
		this.smallScreen = window.innerWidth < 768;
	}
}
