import { Component, OnInit, OnChanges, SimpleChanges } from '@angular/core';
import { RestserviceService } from './restservice.service';
import { GlobalConstants } from './global-constants';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})

export class AppComponent implements OnInit, OnChanges {
  title = 'dbConnector';
  public isLoggedIn: boolean = false;
  public isworking: boolean = true;
  public myToken: any;
  private config = require("../assets/env.json");

  constructor(private restService: RestserviceService) {
    console.log('AppComponent: constructor:');
  }

  ngOnChanges(changes: SimpleChanges): void {
    console.log('AppComponent: ngOnChanges', changes);
  }

  ngOnInit() {
    console.log('AppComponent: ngOnInit');
  }


  loginButton() {
    console.log('AppComponent: loginButton');
  }

  login() {
    console.log('AppComponent: login');
    document.location.href = this.config.loginUrl;
  }

  logout() {
    console.log('AppComponent: logout');
    document.location.href = "/logout";
  }
}
