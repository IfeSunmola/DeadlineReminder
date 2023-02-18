import {Component, OnInit} from '@angular/core';
import {INVALID_REQUEST, INVALID_REQUEST_MESSAGE} from "../AppConstants";

@Component({
	selector: 'app-homepage',
	templateUrl: './homepage.component.html',
	styleUrls: ['./homepage.component.scss']
})
export class HomepageComponent implements OnInit{
	invalidRequest = false;
	readonly INVALID_REQUEST_MESSAGE = INVALID_REQUEST_MESSAGE

	ngOnInit(): void {
		this.invalidRequest = localStorage.getItem(INVALID_REQUEST) === "true"
		localStorage.removeItem(INVALID_REQUEST)
	}

}
