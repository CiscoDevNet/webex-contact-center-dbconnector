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
    console.log('LoginComponent: login:');
    const url = this.config.loginUrl
      + "response_type=" + this.config.response_type
      + "&client_id=" + this.config.client_id
      + "&redirect_uri=" + this.config.redirect_uri
      + "&scope=" + this.config.scope
      + "&state=" + this.config.state;
    console.log('LoginComponent: login:url:', url);

    document.location.href = url;
    // this.config.loginUrl
    //   + "&response_type=" + + this.config.response_type
    //   + "&client_id=" + + this.config.client_id
    //   + "&redirect_uri=" + + this.config.redirect_uri
    //   + "&scope=" + + this.config.scope
    //   + "&state=" + + this.config.state;
  }

}
