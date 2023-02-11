import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";

@Injectable({
	providedIn: 'root'
})
export class AuthService {
	readonly BASE_URL: string = "http://localhost:9191/auth";

	constructor(private http: HttpClient) {
	}

	registerUser(user: any) {
		return this.http.post(`${this.BASE_URL}/register`, user);
	}

	/*
	* I don't want to send the entire request body to the backend if the email already exists
	* So, First check if the email exists. But, this puts a strain on the database because
	* the async validator will check the db for the email everytime the user leaves the email field.
	* I don't think it's that big of an issue, but still.
	* Use caching to get around this or remove it.
	* */
	emailExists(email: string): any {
		return this.http.post(`${this.BASE_URL}/email-exists`, email);
	}
}
