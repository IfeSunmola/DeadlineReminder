import {ChangeDetectorRef, Component, OnInit} from '@angular/core';
import {LoadingService} from "../../services/loading.service";

@Component({
	selector: 'app-loading',
	templateUrl: './loading.component.html',
	styleUrls: ['./loading.component.scss']
})
export class LoadingComponent implements OnInit {
	showSpinner = false;

	constructor(private loadingService: LoadingService, private cdRef: ChangeDetectorRef) {
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
