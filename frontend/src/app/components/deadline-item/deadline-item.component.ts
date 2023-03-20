import {Component, Input} from '@angular/core';
import {Deadline} from "../../models/deadline";

@Component({
	selector: 'app-deadline-item',
	templateUrl: './deadline-item.component.html',
	styleUrls: ['./deadline-item.component.scss']
})
export class DeadlineItemComponent {
    @Input() deadline: Deadline | undefined;
}
