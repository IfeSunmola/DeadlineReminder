import {Injectable} from '@angular/core';
import {API_URL} from "../../AppConstants";
import {HttpClient} from "@angular/common/http";
import {AccountInfo} from "../../models/account-info";

@Injectable({
	providedIn: 'root'
})
export class AccountService {
	readonly BASE_URL: string = API_URL + "/account";

	constructor(private http: HttpClient) {
	}

	getAccount() {
		return this.http.get<AccountInfo>(`${this.BASE_URL}/get`, {responseType: 'json'});
	}
}
