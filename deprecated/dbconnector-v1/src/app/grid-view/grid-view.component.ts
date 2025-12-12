import { Component, OnInit } from '@angular/core';
import { RestserviceService } from '../restservice.service';
import { GlobalConstants } from '../global-constants';

@Component({
  selector: 'app-grid-view',
  templateUrl: './grid-view.component.html',
  styleUrls: ['./grid-view.component.css']
})
export class GridViewComponent implements OnInit {
  isLoggedIn: boolean;
  endPoint: any = [];
  endpoints: any = [];
  endpoint: any = {};
  basicAuth: any = {};
  selectedEndPoint = '';
  records: any = [{}];
  headers: any = [];
  isworking = true;

  constructor(private restService: RestserviceService) {
    this.isLoggedIn = GlobalConstants.isLoggedIn;
  }

  ngOnInit() {
    this.getBasicAuth();
    this.getEndpoints();

  }
  getBasicAuth(): void {
    console.log('GridViewComponent: getBasicAuth');
    this.restService.getBasicAuth()
      .subscribe(data => {
        this.basicAuth = data;
      });
  }

  getEndpoints(): void {
    console.log('GridViewComponent: getEndpoints');
    this.restService.getEndpoints()
      .subscribe(data => {
        this.endpoints = data;
        console.log('GridViewComponent: getEndpoints', this.endpoints);
        this.isLoggedIn = true;
        this.isworking = false;
      });
  }
  executeEndPoint(event: any) {
    console.log('GridViewComponent: executeEndPoint', event.target.value);
    let tmp: any = [];
    this.records = [];
    this.restService.executeEndPoint(event.target.value)
      .subscribe(data => {
        const check: any = data;
        if (check.length > 0) {
          tmp = data;
        } else {
          tmp.push(data);
        }
        let count = 0;
        tmp.forEach((element: {}) => {
          if (count++ === 0) {
            this.headers = Object.keys(element);
          }
          this.records.push(Object.values(element));
          console.log('GridViewComponent: executeEndPoint: this.records4: ', Object.values(element));
        });

      });
  }
}
