import {Component} from '@angular/core';
import {NewDeadlineComponent} from "./new-deadline/new-deadline.component";

@Component({
	selector: 'app-user-home',
	templateUrl: './user-home.component.html',
	styleUrls: ['./user-home.component.scss']
})
export class UserHomeComponent {

	openNewDeadlineModal(content: NewDeadlineComponent) {
		content.openModal();
	}
}
