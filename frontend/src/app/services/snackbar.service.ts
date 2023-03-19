import {Injectable} from '@angular/core';
import {MatSnackBar, MatSnackBarRef} from "@angular/material/snack-bar";
import {SnackbarComponent} from "../components/snackbar/snackbar.component";
import {SnackbarData} from "../models/snackbar-data";

@Injectable({
	providedIn: 'root'
})
export class SnackbarService {

	constructor(private snackbar: MatSnackBar) {
	}

	new(message: string, action: string, duration: number = 10000, typeClass: string = "default") {
		// Using custom colors: https://stackoverflow.com/a/74704599/18902234
		this.snackbar.open(message, action, {
			duration: duration,
			panelClass: [typeClass]
		})
	}

	newFromComponent(data: SnackbarData, duration: number = 10000): MatSnackBarRef<SnackbarComponent> {
		return this.snackbar.openFromComponent(SnackbarComponent, {
			data: data,
			duration: duration,
		})
	}
}