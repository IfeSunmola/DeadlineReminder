import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {API_URL} from "../AppConstants";

@Injectable({
	providedIn: 'root'
})
export class TimesService {
	readonly BASE_URL: string = API_URL + "/static";

	constructor(private http: HttpClient) {
	}

	get reminderTimes() {
		return this.http.get<{ name: string, checkedByDefault: boolean }[]>(`${this.BASE_URL}/reminder-times`,
			{responseType: 'json'});
	}

	get dueTimes() {
		return this.http.get<{ name: string }[]>(`${this.BASE_URL}/due-times`, {responseType: 'json'});
	}
}
