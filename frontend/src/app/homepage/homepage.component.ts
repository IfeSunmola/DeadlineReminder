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
		this.invalidRequest = sessionStorage.getItem(INVALID_REQUEST) === "true"
		sessionStorage.removeItem(INVALID_REQUEST)

		this.invalidResetLink = sessionStorage.getItem(INVALID_RESET_LINK) === "true"
		sessionStorage.removeItem(INVALID_RESET_LINK)

	}

}
