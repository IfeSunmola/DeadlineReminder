import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {API_URL} from "../../AppConstants";
import {NewDeadline} from "../../models/new-deadline";

@Injectable({
	providedIn: 'root'
})
export class DeadlineService {
	readonly BASE_URL: string = API_URL + "/deadlines";

	constructor(private http: HttpClient) {
	}

	saveDeadline(newDeadline: NewDeadline) {
		return this.http.post(`${this.BASE_URL}/save`, newDeadline, {responseType: 'json'});
	}
}
