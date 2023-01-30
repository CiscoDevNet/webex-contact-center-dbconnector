import { Component } from '@angular/core';
import { GlobalConstants } from '../global-constants';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  public myToken: any;
  private config = require("../../assets/env.json");

  constructor() {
    this.myToken = GlobalConstants.token;
  }



  login() {
    document.location.href = this.config.loginUrl;
  }

}
