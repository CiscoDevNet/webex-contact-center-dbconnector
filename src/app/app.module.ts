import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { HttpClientModule } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { ReactiveFormsModule } from '@angular/forms';
import { CookieService } from 'ngx-cookie-service';

import { AppComponent } from './app.component';
import { ConnectorComponent } from './connector/connector.component';
import { EndpointComponent } from './endpoint/endpoint.component';
import { LogoutComponent } from './logout/logout.component';
import { HelpComponent } from './help/help.component';
import { SupportComponent } from './support/support.component';
import { AboutComponent } from './about/about.component';
import { LoginComponent } from './login/login.component';
import { LoggedinComponent } from './loggedin/loggedin.component';
import { GridViewComponent } from './grid-view/grid-view.component';
// import { GlobalConstants } from './global-constants';


@NgModule({
  declarations: [
    // GlobalConstants,
    AppComponent,
    ConnectorComponent,
    EndpointComponent,
    LogoutComponent,
    HelpComponent,
    SupportComponent,
    AboutComponent,
    LoginComponent,
    LoggedinComponent,
    GridViewComponent,
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    FormsModule,
    ReactiveFormsModule,
    RouterModule.forRoot([
      { path: '', component: AppComponent },
      { path: 'logout', component: LogoutComponent },
      { path: 'help', component: HelpComponent },
      { path: 'support', component: SupportComponent },
      { path: 'about', component: AboutComponent },
      { path: 'login', component: LoginComponent },
      { path: 'connector', component: ConnectorComponent },
      { path: 'endpoint', component: EndpointComponent },
      { path: 'loggedin', component: LoggedinComponent },
      { path: 'GridView', component: GridViewComponent },
    ]),
  ],
  exports: [RouterModule],
  providers: [ CookieService ],
  bootstrap: [AppComponent]
})
export class AppModule { }
