import {Component, OnInit} from '@angular/core';
import {INVALID_REQUEST, INVALID_REQUEST_MESSAGE, INVALID_RESET_LINK, INVALID_RESET_LINK_MESSAGE} from "../AppConstants";

@Component({
	selector: 'app-homepage',
	templateUrl: './homepage.component.html',
	styleUrls: ['./homepage.component.scss']
})
export class HomepageComponent implements OnInit{
	invalidRequest = false;
	readonly INVALID_REQUEST_MESSAGE = INVALID_REQUEST_MESSAGE
	// password reset link has expired or is not valid
	invalidResetLink = false
	readonly INVALID_RESET_LINK_MESSAGE = INVALID_RESET_LINK_MESSAGE

	ngOnInit(): void {
		this.invalidRequest = localStorage.getItem(INVALID_REQUEST) === "true"
		localStorage.removeItem(INVALID_REQUEST)

		this.invalidResetLink = localStorage.getItem(INVALID_RESET_LINK) === "true"
		localStorage.removeItem(INVALID_RESET_LINK)

	}

}
