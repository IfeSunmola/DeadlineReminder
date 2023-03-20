export class NewDeadline {
	constructor(private title: string, private dueDate: Date, private dueTime: string,
				private otherPeopleSee: boolean, private email: string,
				private timesToRemind: []) {
	}
}