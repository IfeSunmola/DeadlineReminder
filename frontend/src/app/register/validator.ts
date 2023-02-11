import {AbstractControl, AsyncValidator, AsyncValidatorFn, ValidationErrors, ValidatorFn} from "@angular/forms";
import {Injectable} from "@angular/core";
import {AuthService} from "../services/auth.service";
import {catchError, map, Observable, of} from "rxjs";

export const MatchingPasswords: ValidatorFn = (control: AbstractControl): ValidationErrors | null => {
	const password = control.get('password')?.value;
	const confirmPassword = control.get('confirmPassword')?.value;

	return password === confirmPassword ? null : {passwordsNotMatching: true};
};

@Injectable({providedIn: 'root'})
export class EmailValidator implements AsyncValidator {
	constructor(private authService: AuthService) {
	}

	validate(control: AbstractControl): Observable<ValidationErrors | null> {
		return this.authService.emailExists(control.value)
			.pipe(
				map(emailExists => (emailExists ? {emailInDb: true} : null)),
				catchError(() => of(null))
			);
	}
}