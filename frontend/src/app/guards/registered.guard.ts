import {Injectable} from '@angular/core';
import {ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot, UrlTree} from '@angular/router';
import {Observable} from 'rxjs';
import {AuthService} from "../services/auth.service";

@Injectable({
	providedIn: 'root'
})
export class RegisteredGuard implements CanActivate {
	constructor(private authService: AuthService, private router: Router) {
	}

	canActivate(
		route: ActivatedRouteSnapshot,
		state: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {

		if (this.authService.isAuthenticated()) {
			console.log("RegisteredGuard: User is authenticated")
			this.router.navigate(["/me"]).then()
			return false; // false means that we're not going to the route that we're trying to go to
		}
		return true;
	}
}
