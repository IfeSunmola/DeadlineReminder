import {Injectable} from '@angular/core';
import {API_URL} from "../../AppConstants";
import {HttpClient} from "@angular/common/http";
import {LogBody} from "../../models/log-body";

@Injectable({
	providedIn: 'root'
})
export class LoggerService {
	private readonly BASE_URL: string = API_URL + "/frontend/log";

	constructor(private http: HttpClient) {
	}

	debug(logBody: LogBody) {
		logBody._logLevel = "DEBUG";
		return this.http.post(this.BASE_URL + "/debug", logBody);
	}

	info(logBody: LogBody) {
		logBody._logLevel = "INFO";
		return this.http.post(this.BASE_URL + "/info", logBody);
	}

	warn(logBody: LogBody) {
		logBody._logLevel = "WARN";
		return this.http.post(this.BASE_URL + "/warn", logBody);
	}

	error(logBody: LogBody) {
		logBody._logLevel = "ERROR";
		return this.http.post(this.BASE_URL + "/error", logBody);
	}
}
