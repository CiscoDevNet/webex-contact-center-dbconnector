import { Component, OnInit } from '@angular/core';
import { GlobalConstants } from '../global-constants';

@Component({
  selector: 'app-logout',
  templateUrl: './logout.component.html',
  styleUrls: ['./logout.component.css']
})
export class LogoutComponent implements OnInit {
  isLoggedIn: boolean;
  message: any;

  constructor() {
    this.isLoggedIn = GlobalConstants.isLoggedIn;

  }

  ngOnInit() {
    console.log('LogoutComponent: ngOnInit', this.isLoggedIn);
    this.isLoggedIn = GlobalConstants.isLoggedIn;
    this.message = '';
  }

  myLogout(): void {
    console.log('LogoutComponent: myLogout');
    window.location.href = '/oauthLogout' ;

  }
}
