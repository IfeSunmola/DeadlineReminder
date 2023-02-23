import {Component, OnInit} from '@angular/core';

@Component({
	selector: 'app-welcome-msg',
	templateUrl: './welcome-msg.component.html',
	styleUrls: ['./welcome-msg.component.scss']
})
export class WelcomeMsgComponent implements OnInit {
	today: string = "";

	ngOnInit() {
		const formatter = new Intl.DateTimeFormat('en', {month: 'short', day: 'numeric', year: 'numeric'});
		this.today = formatter.format(new Date());
	}

}
