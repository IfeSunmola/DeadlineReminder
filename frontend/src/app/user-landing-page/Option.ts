export class Option {
	constructor(private _name: string, private _isChecked: boolean) {
	}

	get name(): string {
		return this._name;
	}

	get isChecked(): boolean {
		return this._isChecked;
	}

	set name(value: string) {
		this._name = value;
	}
}