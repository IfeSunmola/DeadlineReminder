import {Injectable} from '@angular/core';
import {HttpErrorResponse, HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from '@angular/common/http';
import {catchError, Observable, of, throwError} from 'rxjs';
import {DISABLED_ACCOUNT, DISABLED_ACCOUNT_MSG, INVALID_CREDENTIALS_MSG, INVALID_SESSION_MSG} from "../AppConstants";
import {Router} from "@angular/router";
import {AuthService} from "../services/http/auth.service";
import {LoggerService} from "../services/http/logger.service";
import {LogBody} from "../models/log-body";
import {SnackbarService} from "../services/snackbar.service";

@Injectable()
export class AuthInterceptor implements HttpInterceptor {
	private readonly FILE_NAME = "auth-interceptor.service.ts"

	constructor(private router: Router, private authService: AuthService, private logger: LoggerService, private snackbarService: SnackbarService) {
	}

	intercept(request: HttpRequest<unknown>, next: HttpHandler): Observable<HttpEvent<unknown>> {
		// Intercept, add the jwt bearer token, send off
		const authToken = this.authService.getAuthToken()
		if (authToken) {
			const clonedRequest = request.clone({
				setHeaders: {
					Authorization: `Bearer ${authToken}`
				}
			})
			return next.handle(clonedRequest).pipe(catchError(x => this.handleAuthError(x)));
		}
		return next.handle(request).pipe(catchError(x => this.handleAuthError(x)));
	}

	private handleAuthError(err: HttpErrorResponse): Observable<any> {
		// https://stackoverflow.com/a/50970853
		if (err.status === 401 || err.status === 403) {
			if (err.error === null) { // no error message inside "error" json -> Request could not reach the backend controller because of invalid jwt
				// user was already logged in but the jwt became invalid
				this.authService.logout()
				this.router.navigateByUrl("/login").then()
				this.snackbarService.new(INVALID_SESSION_MSG, "OK")
			}
			else { // request reached the backend controller
				// All 401s and 403 that reached backend controller should have an "error" json value
				const errorMessage = err.error.error
				if (errorMessage === DISABLED_ACCOUNT) {
					this.handleDisabledAccount(err)
				}
				else if (errorMessage == INVALID_CREDENTIALS_MSG) {
					this.handleInvalidCredentials(err)
				}
				else {
					this.logger.error(new LogBody(this.FILE_NAME,
						"Unhandled/Unknown unauthorized error",
						`Error: ${JSON.stringify(err)}`)
					).subscribe()
					this.authService.logout()
					this.router.navigateByUrl("/").then()
				}
			}
			return of(err.message); // or EMPTY may be appropriate here
		}
		return throwError(() => err);
	}

	private handleDisabledAccount(error: HttpErrorResponse) {
		const email = error.error.email
		this.authService.logout() // shouldn't really do anything since the user isn't logged-in anyway
		this.snackbarService.new(DISABLED_ACCOUNT_MSG, "OK")

		this.authService.sendVerificationCode(email).subscribe(
			{
				next: (response) => {
					if (response.email !== email) { // random safeguard
						this.logger.error(new LogBody(this.FILE_NAME, "Email in response is different from entered email",
							`Response: ${JSON.stringify(response)}\nEmail: ${email}`)).subscribe()
						this.router.navigate(['/login']).then()
						return
					}
					else {
						this.router.navigateByUrl('/register/verify',
							{state: {email: response.email, codeId: response.codeId}}).then();
						return
					}
				}
			}
		)
	}

	private handleInvalidCredentials(error: HttpErrorResponse) {
		this.logger.error(new LogBody(this.FILE_NAME, "Invalid credentials",
			`Error: ${JSON.stringify(error.error)}`)
		).subscribe()
		this.snackbarService.new(INVALID_CREDENTIALS_MSG, "OK")
		// refresh page https://stackoverflow.com/a/49509706
		this.router.navigateByUrl('/', {skipLocationChange: true}).then(() =>
			this.router.navigate(["/login"]));
	}

}
