import {Injectable} from '@angular/core';
import {ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot, UrlTree} from '@angular/router';
import {Observable} from 'rxjs';
import {AuthService} from "../services/auth.service";
import {LOGIN_NEEDED} from "../AppConstants";


@Injectable({
	providedIn: 'root'
})
export class AuthGuard implements CanActivate {
	constructor(private authService: AuthService, private router: Router) {
	}

	canActivate(
		route: ActivatedRouteSnapshot,
		state: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {
		if (this.authService.isAuthenticated()) {
			console.log("AuthGuard: User is authenticated")
			return true;
		}
		sessionStorage.setItem(LOGIN_NEEDED, "true")
		console.log("AuthGuard: User is not authenticated")
		this.router.navigate(["/login"]).then()
		return false;
	}
}
