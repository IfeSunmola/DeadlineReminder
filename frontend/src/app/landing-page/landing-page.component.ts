import {Component} from '@angular/core';
import {APP_NANE} from "../../constants";
import {Router} from "@angular/router";


@Component({
	selector: 'app-landing-page',
	templateUrl: './landing-page.component.html',
	styleUrls: ['./landing-page.component.scss']
})
export class LandingPageComponent {
	title: string = APP_NANE
	currentUrl: string = "";

	constructor(private router: Router) {
	}

	ngOnInit(): void {
		this.currentUrl = this.router.url;
		console.log(this.currentUrl);
	}
}
