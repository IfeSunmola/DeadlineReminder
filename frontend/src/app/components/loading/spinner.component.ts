import {ChangeDetectorRef, Component, OnInit} from '@angular/core';
import {SpinnerService} from "../../services/spinner.service";

@Component({
	selector: 'app-loading',
	templateUrl: './spinner.component.html',
	styleUrls: ['./spinner.component.scss']
})
export class SpinnerComponent implements OnInit {
	showSpinner = false;

	constructor(private loadingService: SpinnerService, private cdRef: ChangeDetectorRef) {
	}

	ngOnInit(): void {
		this.init()
	}

	private init() {
		this.loadingService.loadingObserver.subscribe((status) => {
			this.showSpinner = status === 'start'
			this.cdRef.detectChanges()
		})
	}
}
