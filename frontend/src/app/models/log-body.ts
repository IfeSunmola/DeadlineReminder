export class LogBody {
	private timeStamp: number;
	private logLevel: string = "";
	private fileName: string;
	private message: string;
	private attachment: string

	constructor(private _fileName: string, private _message: string, private _attachment = "") {
		// loglevel will be set in the logger service
		this.timeStamp = Date.now() / 1000;
		this.fileName = _fileName;
		this.message = _message;
		this.attachment = _attachment;
	}

	set _logLevel(logLevel: string) {
		this.logLevel = logLevel;
	}
}
