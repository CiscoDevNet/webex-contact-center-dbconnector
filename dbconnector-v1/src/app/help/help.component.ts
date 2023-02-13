import { Component, OnInit } from '@angular/core';
import { GlobalConstants } from '../global-constants';

@Component({
  selector: 'app-help',
  templateUrl: './help.component.html',
  styleUrls: ['./help.component.css']
})

export class HelpComponent implements OnInit {
  isLoggedIn: boolean;

  constructor() {
    this.isLoggedIn = GlobalConstants.isLoggedIn;
  }

  ngOnInit() {
    console.log('HelpComponent: ngOnInit', this.isLoggedIn);
    this.isLoggedIn = GlobalConstants.isLoggedIn;
  }

}
