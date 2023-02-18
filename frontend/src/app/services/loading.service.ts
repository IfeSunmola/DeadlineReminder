import {Injectable} from '@angular/core';
import {BehaviorSubject, Observable} from "rxjs";

@Injectable({
	providedIn: 'root'
})
export class LoadingService {
	private _loading = new BehaviorSubject<string>('');
	private count = 0;

	constructor() {
	}

	get loadingObserver(): Observable<string> {
		return this._loading.asObservable()
	}

	requestStarted() {
		if (++this.count === 1) {
			this._loading.next('start')
		}
	}

	requestEnded() {
		if (this.count === 0 || --this.count === 0) {
			this._loading.next('stop')
		}
	}

	resetSpinner() {
		this.count = 0;
		this._loading.next('stop')
	}

}
