import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {FormGroup} from "@angular/forms";

@Injectable({
	providedIn: 'root'
})
export class AuthService {
	BASE_URL = "http://localhost:8080/auth";

	constructor(private http: HttpClient) {
	}

	register(registerForm: FormGroup): Observable<any> {
		return this.http.post(`${this.BASE_URL}/register`, registerForm.value);
	}

	login(loginForm: FormGroup): Observable<any> {
		return this.http.post(`${this.BASE_URL}/generate-token`, loginForm.value, {responseType: 'text'});
	}

	getCurrentUser(): Observable<string> {
		return this.http.get<string>(`${this.BASE_URL}/get-user`);
	}
}
