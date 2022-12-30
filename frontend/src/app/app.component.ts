import {Component} from '@angular/core';
import {AuthService} from "./services/auth.service";
@Component({
	selector: 'app-root',
	templateUrl: './app.component.html',
	styleUrls: ['./app.component.scss']
})
export class AppComponent {
	title = 'Deadline Reminder';

	constructor(private authService: AuthService) {
	}

	// register(data: RegisterData) {
	// 	this.authService.register(data).subscribe(
	// 		response => {
	// 			console.log(response);
	// 		},
	// 	);
	// }
	//
	// getCurrentUser() {
	// 	this.authService.getCurrentUser().subscribe(
	// 		email => {
	// 			console.log(`Email: ${email}`);
	// 		}
	// 	);
	// };
}
