import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {API_URL, AUTH_TOKEN} from "../AppConstants";
import {RegisterUserResponse} from "../models/register-user-response";
import {VerifyCodeData} from "../models/verify-code-data";
import {LoginData} from "../models/login-data";
import {RegisterData} from "../models/register-data";
import {GenerateTokenResponse} from "../models/generate-token-response";
import {PasswordResetData} from "../models/password-reset-data";

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
		return this.http.post<{message: string}>(`${this.BASE_URL}/register/verify`, verifyCodeData, {responseType: 'json'});
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
		this.deleteAuthToken();
		//TODO: invalidate the token on the server
	}

	isAuthenticated(): boolean {
		// if the AUTH_TOKEN is invalid, a 401 or 403 will be thrown, the AuthInterceptor will redirect to the login page
		return this.getAuthToken() != null;
	}

	sendVerificationCode(email: string) {
		return this.http.post<RegisterUserResponse>(`${this.BASE_URL}/send-verification-email`, email, {responseType: 'json'})
	}

	sendPasswordResetMail(email: string) {
		return this.http.post(`${this.BASE_URL}/send-reset-password-email`, email)
	}

	verifyPasswordResetCode(token: string) {
		return this.http.get(`${this.BASE_URL}/reset-password/verify?token=${token}`, {responseType: 'text'})
	}

	confirmPasswordReset(passwordResetData: PasswordResetData) {
		return this.http.put<PasswordResetData>(`${this.BASE_URL}/confirm-reset`, passwordResetData, {responseType: 'json'})
	}

	getAuthToken() {
		return localStorage.getItem(AUTH_TOKEN);
	}

	setAuthToken(token: string) {
		localStorage.setItem(AUTH_TOKEN, token);
	}

	deleteAuthToken() {
		localStorage.removeItem(AUTH_TOKEN);
	}
}
