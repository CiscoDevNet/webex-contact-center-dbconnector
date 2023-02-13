import { Component, OnInit } from '@angular/core';
import { GlobalConstants } from '../global-constants';

@Component({
  selector: 'app-about',
  templateUrl: './about.component.html',
  styleUrls: ['./about.component.css']
})

export class AboutComponent implements OnInit {
  isLoggedIn: boolean;

  constructor() {
    this.isLoggedIn = GlobalConstants.isLoggedIn;
  }

  ngOnInit() {
    console.log('AboutComponent: ngOnInit', this.isLoggedIn);
    this.isLoggedIn = GlobalConstants.isLoggedIn;
  }

}
