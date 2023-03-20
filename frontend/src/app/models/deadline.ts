export class Deadline {
	/*
	* For some weird ass reason, I have to make some fields public so they can
	* be usable from the html template.
	*
	* Getters aren't working
	* */
	constructor(private deadlineId: number, public title: string, public dueDateTime: Date,
				private otherPeopleSee: Boolean, private completed: Boolean, private pastDue: Boolean) {
	}
}
