import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule } from '@angular/common/http';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { ConnectorComponent } from './connector/connector.component';
import { EndpointComponent } from './endpoint/endpoint.component';
import { GridViewComponent } from './grid-view/grid-view.component';
import { LoggedinComponent } from './loggedin/loggedin.component';
import { LoginComponent } from './login/login.component';
import { LogoutComponent } from './logout/logout.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MysqlComponent } from './connector/mysql/mysql.component';
import { SqlserverComponent } from './connector/sqlserver/sqlserver.component';
import { OracleComponent } from './connector/oracle/oracle.component';

@NgModule({
  declarations: [
    AppComponent,
    ConnectorComponent,
    EndpointComponent,
    GridViewComponent,
    LoggedinComponent,
    LoginComponent,
    LogoutComponent,
    SqlserverComponent,
    MysqlComponent,
    OracleComponent,
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    AppRoutingModule,
    FormsModule,
    ReactiveFormsModule,
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
