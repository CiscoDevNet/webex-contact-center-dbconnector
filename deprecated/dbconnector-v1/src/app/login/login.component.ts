import { Component, OnInit } from '@angular/core';
import { GlobalConstants } from '../global-constants';


import { Router } from '@angular/router';


@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {
  isLoggedIn: boolean;

  constructor(private router: Router) { }

  ngOnInit() {
    this.isLoggedIn = GlobalConstants.isLoggedIn;
  }

  clickMe(event: Event) {
    console.log('clickMe', event);
  }
}
