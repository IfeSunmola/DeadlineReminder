import {Injectable} from '@angular/core';
import {ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot, UrlTree} from '@angular/router';
import {Observable} from 'rxjs';
import {AuthService} from "../services/auth.service";
import {LogBody} from "../models/log-body";
import {LoggerService} from "../logger.service";

@Injectable({
	providedIn: 'root'
})
export class RegisteredGuard implements CanActivate {
	private readonly FILE_NAME = "registered.guard.ts"

	constructor(private authService: AuthService, private router: Router, private logger: LoggerService) {
	}

	canActivate(
		route: ActivatedRouteSnapshot,
		state: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {

		if (this.authService.isAuthenticated()) {
			this.logger.debug(new LogBody(this.FILE_NAME,
				"User is authenticated, redirecting to /me")
			).subscribe()
			this.router.navigate(["/me"]).then()
			return false; // false means that we're not going to the route that we're trying to go to
		}
		return true;
	}
}
