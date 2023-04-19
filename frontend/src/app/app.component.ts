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
				"1. Development of this site has been paused on April 19, 2023 because of my sudden interest in compiler theory and programming languages.",
				"2. Only user authentication has been implemented.",
				"3. This message will be shown every 10 minutes.",
				`4. Repo: 
					<a target='_blank' href='https://github.com/IfeSunmola/DeadlineReminder'>
						https://github.com/IfeSunmola/DeadlineReminder
					</a>`
			]
		const snackbarData = {
			title: "Note: April 19th, 2023",
			messages: messages,
			action: "GOT IT"
		}
		timer(0, 600000).subscribe( // show every 10 minutes
			() => {
				this.snackbarService.newFromComponent(snackbarData, 20000) // hide after 20 seconds
			}
		)
	}

	@HostListener('window:resize', ['$event'])
	onWindowResize() {
		this.smallScreen = window.innerWidth < 768;
	}
}
