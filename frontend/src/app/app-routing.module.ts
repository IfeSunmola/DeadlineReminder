import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {HomepageComponent} from "./homepage/homepage.component";
import {LoginComponent} from "./login/login.component";
import {RegisterComponent} from "./register/register.component";
import {UserHomeComponent} from "./user-home/user-home.component";
import {VerifyComponent} from "./register/verify/verify.component";
import {AuthGuard} from "./auth.guard";

const routes: Routes = [
	{
		path: '',
		title: 'Welcome',
		component: HomepageComponent,
		pathMatch: 'full'
	},
	{
		path: 'login',
		title: 'Login',
		component: LoginComponent
	},
	{
		path: 'register',
		title: 'Register',
		component: RegisterComponent
	},
	{
		path: 'register/verify',
		title: 'Verify your account',
		component: VerifyComponent
	},
	{
		path: 'me',
		title: 'Your dashboard',
		component: UserHomeComponent,
		canActivate: [AuthGuard]
	},

];

@NgModule({
	imports: [RouterModule.forRoot(routes)],
	exports: [RouterModule]
})
export class AppRoutingModule {
}
