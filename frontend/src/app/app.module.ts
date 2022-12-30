import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';

import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {NgbModule} from '@ng-bootstrap/ng-bootstrap';
import {LandingPageComponent} from './landing-page/landing-page.component';
import {LoginFormComponent} from './landing-page/login-form/login-form.component';
import {RegisterFormComponent} from './landing-page/register-form/register-form.component';
import {UserLandingPageComponent} from './user-landing-page/user-landing-page.component';
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {NewDeadlineComponent} from './user-landing-page/new-deadline/new-deadline.component';
import {HttpClientModule} from "@angular/common/http";
import { ErrorPageComponent } from './error-page/error-page.component';


@NgModule({
	declarations: [
		AppComponent,
		LandingPageComponent,
		LoginFormComponent,
		RegisterFormComponent,
		UserLandingPageComponent,
		NewDeadlineComponent,
  ErrorPageComponent
	],
	imports: [
		BrowserModule,
		AppRoutingModule,
		NgbModule,
		FormsModule,
		HttpClientModule,
		ReactiveFormsModule,
	],
	providers: [],
	bootstrap: [AppComponent]
})
export class AppModule {
}
