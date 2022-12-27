import {Component} from '@angular/core';
import {NewDeadlineComponent} from "./new-deadline/new-deadline.component";

@Component({
	selector: 'app-user-landing-page',
	templateUrl: './user-landing-page.component.html',
	styleUrls: ['./user-landing-page.component.scss']
})
export class UserLandingPageComponent {
	openNewDeadlineModal(content: NewDeadlineComponent) {
		content.openModal();
	}
}

