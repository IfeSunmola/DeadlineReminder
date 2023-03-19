import {Component, Inject} from '@angular/core';
import {MAT_SNACK_BAR_DATA, MatSnackBarRef} from "@angular/material/snack-bar";
import {SnackbarData} from "../../models/snackbar-data";

@Component({
	selector: 'app-snackbar',
	templateUrl: './snackbar.component.html',
	styleUrls: ['./snackbar.component.scss']
})
export class SnackbarComponent {
	title = ""
	messages: string[] = []
	action = ""

	constructor(@Inject(MAT_SNACK_BAR_DATA) private data: SnackbarData, private snackRef: MatSnackBarRef<SnackbarComponent>) {
	}

	ngOnInit(): void {
		this.title = this.data.title
		this.messages = this.data.messages
		this.action = this.data.action
	}

	actionClicked() {
		this.snackRef.dismiss()
	}
}
