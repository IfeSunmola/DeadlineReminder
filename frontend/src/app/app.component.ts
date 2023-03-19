import {Component, HostListener, OnInit} from '@angular/core';

@Component({
	selector: 'app-root',
	templateUrl: './app.component.html',
	styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit {
	smallScreen = false;

	ngOnInit(): void {
		this.smallScreen = window.innerWidth < 768;
	}

	@HostListener('window:resize', ['$event'])
	onWindowResize() {
		this.smallScreen = window.innerWidth < 768;
	}
}
