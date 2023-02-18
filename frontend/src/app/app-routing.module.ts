import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {HomepageComponent} from "./homepage/homepage.component";
import {LoginComponent} from "./login/login.component";
import {RegisterComponent} from "./register/register.component";
import {UserHomeComponent} from "./user-home/user-home.component";
import {VerifyComponent} from "./register/verify/verify.component";
import {AuthGuard} from "./guards/auth.guard";
import {RegisteredGuard} from "./guards/registered.guard";
import {ResetPasswordComponent} from "./reset-password/reset-password.component";
import {ConfirmResetComponent} from "./reset-password/confirm-reset/confirm-reset.component";

const routes: Routes = [
	{
		path: '',
		title: 'Welcome',
		component: HomepageComponent,
		pathMatch: 'full',
		canActivate: [RegisteredGuard]
	},
	{
		path: 'login',
		title: 'Login',
		component: LoginComponent,
		canActivate: [RegisteredGuard]
	},
	{
		path: 'reset-password',
		title: 'Reset your password',
		component: ResetPasswordComponent,
		canActivate: [RegisteredGuard],
	},
	{
		path: 'reset-password/confirm-reset',
		title: 'Reset your password',
		component: ConfirmResetComponent,
		canActivate: [RegisteredGuard],
	},
	{
		path: 'register',
		title: 'Register',
		component: RegisterComponent,
		canActivate: [RegisteredGuard]
	},
	{
		path: 'register/verify',
		title: 'Verify your account',
		component: VerifyComponent,
		canActivate: [RegisteredGuard]
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
