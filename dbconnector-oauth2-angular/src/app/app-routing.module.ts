import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AppComponent } from './app.component';
import { ConnectorComponent } from './connector/connector.component';
import { EndpointComponent } from './endpoint/endpoint.component';
import { GridViewComponent } from './grid-view/grid-view.component';
import { LoggedinComponent } from './loggedin/loggedin.component';
import { LoginComponent } from './login/login.component';
import { LogoutComponent } from './logout/logout.component';

const routes: Routes = [
  { path: '', component: LoginComponent },
  { path: 'mylogout', component: LogoutComponent },
  { path: 'mylogin', component: LoginComponent },
  { path: 'connector', component: ConnectorComponent },
  { path: 'endpoint', component: EndpointComponent },
  { path: 'loggedin', component: LoggedinComponent },
  { path: 'grid-view', component: GridViewComponent },
  { path: 'help', loadChildren: () => import('./modules/help123/help123.module').then(m => m.Help123Module) },
  { path: 'support', loadChildren: () => import('./modules/support/support.module').then(m => m.SupportModule) },
  { path: 'about', loadChildren: () => import('./modules/about/about.module').then(m => m.AboutModule) },


];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
  bootstrap: [AppComponent]
})
export class AppRoutingModule { }
