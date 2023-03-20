export class LogBody {
	// loglevel will be properly set in the logger service
	constructor(private fileName: string, private message: string, private attachment = "",
				private timeStamp = Date.now() / 1000, private logLevel = ""
	) {
	}

	set _logLevel(logLevel: string) {
		this.logLevel = logLevel;
	}
}
