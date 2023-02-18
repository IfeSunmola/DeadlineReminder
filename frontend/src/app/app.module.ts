import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';

import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {NgbModule} from '@ng-bootstrap/ng-bootstrap';
import {HomepageComponent} from './homepage/homepage.component';
import {NavbarComponent} from './navbar/navbar.component';
import {LoginComponent} from './login/login.component';
import {RegisterComponent} from './register/register.component';
import {UserHomeComponent} from './user-home/user-home.component';
import {NewDeadlineComponent} from './user-home/new-deadline/new-deadline.component';
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {ScreenErrorComponent} from './screen-error/screen-error.component';
import {VerifyComponent} from './register/verify/verify.component';
import {HTTP_INTERCEPTORS, HttpClientModule} from "@angular/common/http";
import {AuthInterceptor} from "./interceptors/auth-interceptor.service";
import {LoadingComponent} from './loading/loading.component';
import {LoadingInterceptor} from "./interceptors/loading.interceptor";
import {ResetPasswordComponent} from './reset-password/reset-password.component';
import {ConfirmResetComponent} from './reset-password/confirm-reset/confirm-reset.component';

@NgModule({
	declarations: [
		AppComponent,
		HomepageComponent,
		NavbarComponent,
		LoginComponent,
		RegisterComponent,
		UserHomeComponent,
		NewDeadlineComponent,
		ScreenErrorComponent,
		VerifyComponent,
		LoadingComponent,
		ResetPasswordComponent,
		ConfirmResetComponent,
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
