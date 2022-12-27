import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';

import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {NgbModule} from '@ng-bootstrap/ng-bootstrap';
import {LandingPageComponent} from './landing-page/landing-page.component';
import {LoginFormComponent} from './landing-page/login-form/login-form.component';
import {RegisterFormComponent} from './landing-page/register-form/register-form.component';
import {UserLandingPageComponent} from './user-landing-page/user-landing-page.component';
import {FormsModule} from "@angular/forms";
import {NewDeadlineComponent} from './user-landing-page/new-deadline/new-deadline.component';

@NgModule({
	declarations: [
		AppComponent,
		LandingPageComponent,
		LoginFormComponent,
		RegisterFormComponent,
		UserLandingPageComponent,
		NewDeadlineComponent
	],
	imports: [
		BrowserModule,
		AppRoutingModule,
		NgbModule,
		FormsModule
	],
	providers: [],
	bootstrap: [AppComponent]
})
export class AppModule {
}
