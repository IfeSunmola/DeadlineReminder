import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { LandingPageComponent } from './landing-page/landing-page.component';
import { NavbarComponent } from './navbar/navbar.component';
import { LoginComponent } from './login/login.component';
import { RegisterComponent } from './register/register.component';
import { UserHomeComponent } from './user-home/user-home.component';
import { NewDeadlineComponent } from './user-home/new-deadline/new-deadline.component';
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import { ScreenErrorComponent } from './screen-error/screen-error.component';

@NgModule({
  declarations: [
    AppComponent,
    LandingPageComponent,
    NavbarComponent,
    LoginComponent,
    RegisterComponent,
    UserHomeComponent,
    NewDeadlineComponent,
    ScreenErrorComponent,
  ],
	imports: [
		BrowserModule,
		AppRoutingModule,
		NgbModule,
		FormsModule,
		ReactiveFormsModule
	],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
