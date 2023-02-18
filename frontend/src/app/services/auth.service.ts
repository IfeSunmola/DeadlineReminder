import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {API_URL, AUTH_TOKEN} from "../AppConstants";
import {RegisterUserResponse} from "../models/register-user-response";
import {VerifyCodeData} from "../models/verify-code-data";
import {LoginData} from "../models/login-data";
import {RegisterData} from "../models/register-data";
import {GenerateTokenResponse} from "../models/generate-token-response";

@Injectable({
	providedIn: 'root'
})
export class AuthService {
	readonly BASE_URL: string = API_URL + "/auth";

	constructor(private http: HttpClient) {
	}

	registerUser(registerData: RegisterData) {
		// the json response will be parsed to RegisterUserResponse
		return this.http.post<RegisterUserResponse>(`${this.BASE_URL}/register`, registerData, {responseType: 'json'});
	}

	verifyCode(verifyCodeData: VerifyCodeData) {
		return this.http.post(`${this.BASE_URL}/register/verify`, verifyCodeData, {responseType: 'text'});
	}

	/*
	* Too expensive, use caching or remove it
	* */
	emailExists(email: string): any {
		return this.http.post(`${this.BASE_URL}/email-exists`, email, {headers: {skip: "true"}});
	}

	login(loginData: LoginData) {
		return this.http.post <GenerateTokenResponse>(`${this.BASE_URL}/generate-token`, loginData, {responseType: 'json'})
	}

	logout() {
		localStorage.removeItem(AUTH_TOKEN);
	}

	isAuthenticated(): boolean {
		// if the AUTH_TOKEN is invalid, a 401 or 403 will be thrown, the AuthInterceptor will redirect to the login page
		return !!localStorage.getItem(AUTH_TOKEN)
	}

	sendVerificationCode(email: string) {
		return this.http.post<RegisterUserResponse>(`${this.BASE_URL}/send-verification-email`, email, {responseType: 'json'})
	}
}
