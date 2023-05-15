import { Injectable, Component, OnInit, Input, Output, EventEmitter } from '@angular/core';
import { RestserviceService } from '../../restservice.service';

@Component({
  selector: 'app-oracle',
  templateUrl: './oracle.component.html',
  styleUrls: ['./oracle.component.css']
})
export class OracleComponent implements OnInit {
  @Input() connector: any;
  isworking = false;
  showTab1 = true;
  showTab2: boolean = false;

  constructor(private restService: RestserviceService) { }
  @Output() onDataChange: EventEmitter<any> = new EventEmitter();

  ngOnInit(): void {
    console.log("OracleComponent: ngOnInit:connector:", this.connector);
  }

  showServerTabButton(tab: string) {
    this.showTab1 = false;
    this.showTab2 = false;

    if (tab === 'Server') {
      this.showTab1 = true;
    }
    if (tab === 'ConnectionPool') {
      this.showTab2 = true;
    }
  }

  updateConnectionString(): void {
    console.log('OracleComponent: connectionString');
    this.connector.connectionString = 'jdbc:oracle:thin:@' + this.connector.hostname + ':' + this.connector.port + '/' + this.connector.database + '';
  }

  saveConnection(): void {
    console.log('OracleComponent: saveConnection');
    this.onDataChange.emit("isworking");
    this.isworking = true;
    this.restService.postConnector(this.connector)
      .subscribe(data => {
        this.onDataChange.emit(data);
        this.isworking = false;
      });
  }
}
