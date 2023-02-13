import {Injectable} from '@angular/core';
import {
	HttpRequest,
	HttpHandler,
	HttpEvent,
	HttpInterceptor, HttpErrorResponse
} from '@angular/common/http';
import {catchError, Observable, of, throwError} from 'rxjs';
import {AUTH_TOKEN} from "../AppConstants";
import {Router} from "@angular/router";
import {AuthService} from "../services/auth.service";

@Injectable()
export class AuthInterceptor implements HttpInterceptor {

	constructor(private router: Router, private authService: AuthService) {
	}

	private handleAuthError(err: HttpErrorResponse): Observable<any> {
		//handle your auth error or rethrow. https://stackoverflow.com/a/50970853
		if (err.status === 401 || err.status === 403) {
			console.log("AuthInterceptor: UNAUTHORIZED: " + err.status)
			this.authService.logout();
			this.router.navigateByUrl(`/login`).then();
			// if you've caught / handled the error, you don't want to rethrow it unless you also want downstream
			// consumers to have to handle it as well.
			return of(err.message); // or EMPTY may be appropriate here
		}
		return throwError(() => err);
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
		} else {
			return next.handle(request).pipe(catchError(x => this.handleAuthError(x)));
		}
	}
}
