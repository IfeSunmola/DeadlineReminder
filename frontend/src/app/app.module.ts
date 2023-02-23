import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';

import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {NgbModule} from '@ng-bootstrap/ng-bootstrap';
import {HomepageComponent} from './components/homepage/homepage.component';
import {NavbarComponent} from './components/navbar/navbar.component';
import {LoginComponent} from './components/login/login.component';
import {RegisterComponent} from './components/register/register.component';
import {UserHomeComponent} from './components/user-home/user-home.component';
import {NewDeadlineComponent} from './components/user-home/new-deadline/new-deadline.component';
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {VerifyComponent} from './components/register/verify/verify.component';
import {HTTP_INTERCEPTORS, HttpClientModule} from "@angular/common/http";
import {AuthInterceptor} from "./interceptors/auth-interceptor.service";
import {LoadingComponent} from './components/loading/loading.component';
import {LoadingInterceptor} from "./interceptors/loading.interceptor";
import {ResetPasswordComponent} from './components/reset-password/reset-password.component';
import {ConfirmResetComponent} from './components/reset-password/confirm-reset/confirm-reset.component';
import { PageNotFoundComponent } from './components/page-not-found/page-not-found.component';
import { WelcomeMsgComponent } from './components/welcome-msg/welcome-msg.component';

@NgModule({
	declarations: [
		AppComponent,
		HomepageComponent,
		NavbarComponent,
		LoginComponent,
		RegisterComponent,
		UserHomeComponent,
		NewDeadlineComponent,
		VerifyComponent,
		LoadingComponent,
		ResetPasswordComponent,
		ConfirmResetComponent,
  PageNotFoundComponent,
  WelcomeMsgComponent,
	],
	imports: [
		BrowserModule,
		AppRoutingModule,
		NgbModule,
		FormsModule,
		ReactiveFormsModule,
		HttpClientModule,
	],
	providers: [
		{
			provide: HTTP_INTERCEPTORS,
			useClass: AuthInterceptor,
			multi: true
		},
		{
			provide: HTTP_INTERCEPTORS,
			useClass: LoadingInterceptor,
			multi: true
		},
	],
	bootstrap: [AppComponent]
})
export class AppModule {
}
