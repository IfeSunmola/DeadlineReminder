import {Component, HostListener, OnInit} from '@angular/core';

@Component({
	selector: 'app-root',
	templateUrl: './app.component.html',
	styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit {
	title = 'frontend';

	screenWidth: number | undefined;
	hasValidScreen: boolean = false;

	ngOnInit(): void {
		this.checkScreen();
	}

	@HostListener('window:resize', ['$event'])
	onWindowResize() {
		this.checkScreen();
	}

	private checkScreen(): void {
		const MAX_WIDTH = 768;
		this.screenWidth = window.innerWidth;
		this.hasValidScreen = this.screenWidth < MAX_WIDTH;
	}
}
