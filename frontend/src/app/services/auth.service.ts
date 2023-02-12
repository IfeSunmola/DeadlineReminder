import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {API_URL} from "../AppConstants";
import {RegisterUserResponse} from "../models/register-user-response";
import {VerifyCodeData} from "../models/verify-code-data";
import {LoginData} from "../models/login-data";

@Injectable({
	providedIn: 'root'
})
export class AuthService {
	readonly BASE_URL: string = API_URL + "/auth";

	constructor(private http: HttpClient) {
	}

	registerUser(user: any) {
		// the json response will be parsed to RegisterUserResponse
		return this.http.post<RegisterUserResponse>(`${this.BASE_URL}/register`, user, {responseType: 'json'});
	}

	verifyCode(verifyCodeData: VerifyCodeData) {
		return this.http.post(`${this.BASE_URL}/register/verify`, verifyCodeData, {responseType: 'text'})
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

	login(loginData: LoginData) {
		return this.http.post(`${this.BASE_URL}/generate-token`, loginData, {responseType: 'text'})
	}
}
