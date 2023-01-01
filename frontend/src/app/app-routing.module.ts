import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {GetStartedComponent} from "./get-started/get-started.component";
import {LandingPageComponent} from "./landing-page/landing-page.component";

const routes: Routes = [
	{path: '', component: LandingPageComponent, pathMatch: 'full'},
	{path: 'get-started', component: GetStartedComponent},
];

@NgModule({
	imports: [RouterModule.forRoot(routes)],
	exports: [RouterModule]
})
export class AppRoutingModule {
}
