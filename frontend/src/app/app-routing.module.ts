import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {LandingPageComponent} from "./landing-page/landing-page.component";
import {UserLandingPageComponent} from "./user-landing-page/user-landing-page.component";
import {ErrorPageComponent} from "./error-page/error-page.component";
import {LoginFormComponent} from "./landing-page/login-form/login-form.component";
import {RegisterFormComponent} from "./landing-page/register-form/register-form.component";

const routes: Routes = [
	{
		path: '',
		component: LandingPageComponent,
		children: [
			{path: 'login', component: LoginFormComponent},
			{path: 'register', component: RegisterFormComponent},
		]
	},
	{path: 'me', component: UserLandingPageComponent},
	{path: '**', component: ErrorPageComponent},
]

@NgModule({
	imports: [RouterModule.forRoot(routes)],
	exports: [RouterModule]
})
export class AppRoutingModule {
}
