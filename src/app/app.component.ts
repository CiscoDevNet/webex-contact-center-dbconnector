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
  public isLoggedIn = false;

  constructor(private restService: RestserviceService) {
    this.restService.onLoginChange.subscribe({
      next: (event: any) => {
        console.log('Received message  ', event);
        this.isLoggedIn = event;
        GlobalConstants.isLoggedIn = this.isLoggedIn;
      }
    });
  }

  ngOnChanges(changes: SimpleChanges): void {
    console.log('AppComponent: ngOnChanges', changes);
  }

  ngOnInit() {
    console.log('AppComponent: ngOnInit');
    this.restService.isAuthenticated();
  }


  loginButton() {
    console.log('AppComponent: loginButton');
    if (this.isLoggedIn) {
      // life is good
    } else {
      location.assign('/oauth2/authorization/dbconnector');
    }
  }
}
