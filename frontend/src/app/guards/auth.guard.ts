import {Injectable} from '@angular/core';
import {ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot, UrlTree} from '@angular/router';
import {Observable} from 'rxjs';
import {AuthService} from "../services/auth.service";
import {LOGIN_NEEDED} from "../AppConstants";
import {LoggerService} from "../logger.service";
import {LogBody} from "../models/log-body";


@Injectable({
	providedIn: 'root'
})
export class AuthGuard implements CanActivate {
	private readonly FILE_NAME: string = "auth.guard.ts";

	constructor(private authService: AuthService, private router: Router, private logger: LoggerService) {
	}

	canActivate(
		route: ActivatedRouteSnapshot,
		state: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {
		if (this.authService.isAuthenticated()) {
			this.logger.debug(new LogBody(this.FILE_NAME, "AuthGuard: User is authenticated")).subscribe();
			return true;
		}
		sessionStorage.setItem(LOGIN_NEEDED, "true")
		this.logger.debug(new LogBody(this.FILE_NAME, "AuthGuard: User is not authenticated")).subscribe();
		this.router.navigate(["/login"]).then()
		return false;
	}
}
