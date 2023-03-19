import {Injectable} from '@angular/core';
import {HttpErrorResponse, HttpEvent, HttpHandler, HttpInterceptor, HttpRequest, HttpResponse} from '@angular/common/http';
import {Observable, tap} from 'rxjs';
import {SpinnerService} from "../services/spinner.service";

@Injectable()
export class LoadingInterceptor implements HttpInterceptor {

	constructor(private loadingService: SpinnerService) {
	}

	intercept(request: HttpRequest<unknown>, next: HttpHandler): Observable<HttpEvent<unknown>> {
		const skipIntercept = request.headers.has("skip")

		if (skipIntercept) {// remove skip immediately so the AuthInterceptor will not skip the api call
			request = request.clone({
				headers: request.headers.delete('skip')
			});
			return next.handle(request)
		}
		this.loadingService.requestStarted()
		return this.handler(next, request)
	}

	private handler(next: HttpHandler, request: HttpRequest<unknown>) {
		return next.handle(request).pipe(
			tap(
				(event) => {
					if (event instanceof HttpResponse) {
						this.loadingService.requestEnded()
					}
				},
				(error: HttpErrorResponse) => {
					this.loadingService.resetSpinner()
					throw error;
				}
			)
		)
	}
}
