import {Injectable} from '@angular/core';
import {MatSnackBar} from "@angular/material/snack-bar";

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
}