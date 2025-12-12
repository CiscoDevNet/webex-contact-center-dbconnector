import { Component, OnInit } from '@angular/core';
import { GlobalConstants } from '../global-constants';

@Component({
  selector: 'app-loggedin',
  templateUrl: './loggedin.component.html',
  styleUrls: ['./loggedin.component.css']
})

export class LoggedinComponent implements OnInit {
  isLoggedIn: boolean;

  constructor() {
    this.isLoggedIn = GlobalConstants.isLoggedIn;
  }

  ngOnInit() {
    this.isLoggedIn = GlobalConstants.isLoggedIn;
  }

}
