import {Injectable} from '@angular/core';
import {
	HttpRequest,
	HttpHandler,
	HttpEvent,
	HttpInterceptor, HttpErrorResponse
} from '@angular/common/http';
import {catchError, Observable, of, throwError} from 'rxjs';
import {AUTH_TOKEN, DISABLED_ACCOUNT, EXPIRED_SESSION} from "../AppConstants";
import {Router} from "@angular/router";
import {AuthService} from "../services/auth.service";

@Injectable()
export class AuthInterceptor implements HttpInterceptor {

	constructor(private router: Router, private authService: AuthService) {
	}

	private handleAuthError(err: HttpErrorResponse): Observable<any> {
		// https://stackoverflow.com/a/50970853
		if (err.status === 401 || err.status === 403) {
			// All 401s and 403 should return "error" message
			const errorMessage = err.error.error
			if (errorMessage === DISABLED_ACCOUNT) {
				this.handleDisabledAccount(err)
			}
			else {
				console.log("2 AuthInterceptor: UNAUTHORIZED: " + err.error.error)
				localStorage.setItem(EXPIRED_SESSION, "true");
				this.authService.logout();
				this.router.navigateByUrl(`/login`).then();
			}
			return of(err.message); // or EMPTY may be appropriate here
		}
		return throwError(() => err);
	}

	private handleDisabledAccount(error: HttpErrorResponse) {
		const email = error.error.email
		console.log("1 AuthInterceptor: UNAUTHORIZED, DISABLED: " + error.error)
		this.authService.logout() // shouldn't really do anything since the user isn't logged-in anyway
		localStorage.setItem(DISABLED_ACCOUNT, "true")

		this.authService.sendVerificationCode(email).subscribe(
			{
				next: (response) => {
					if (response.email !== email) { // random safeguard
						console.log("AuthInterceptor: sendVerificationCode: email mismatch")
						this.router.navigate(['/login']).then()
					}
					else {
						this.router.navigateByUrl('/register/verify',
							{state: {email: response.email, codeId: response.codeId}}).then();
					}
				}
			}
		)
	}

	intercept(request: HttpRequest<unknown>, next: HttpHandler): Observable<HttpEvent<unknown>> {
		// Intercept, add the jwt bearer token, send off
		const authToken = localStorage.getItem(AUTH_TOKEN)
		if (authToken) {
			const clonedRequest = request.clone({
				setHeaders: {
					Authorization: `Bearer ${authToken}`
				}
			})
			return next.handle(clonedRequest).pipe(catchError(x => this.handleAuthError(x)));
		}
		else {
			return next.handle(request).pipe(catchError(x => this.handleAuthError(x)));
		}
	}
}
