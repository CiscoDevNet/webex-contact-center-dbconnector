import { Component, OnInit } from '@angular/core';
import { GlobalConstants } from '../global-constants';

@Component({
  selector: 'app-support',
  templateUrl: './support.component.html',
  styleUrls: ['./support.component.css']
})

export class SupportComponent implements OnInit {
  isLoggedIn: boolean;

  constructor() {
    this.isLoggedIn = GlobalConstants.isLoggedIn;
  }

  ngOnInit() {
    console.log('SupportComponent: ngOnInit', this.isLoggedIn);
    this.isLoggedIn = GlobalConstants.isLoggedIn;
  }

}
