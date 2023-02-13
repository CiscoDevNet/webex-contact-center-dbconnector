import { Injectable, Component, OnInit, Input, Output, EventEmitter } from '@angular/core';
import { RestserviceService } from '../../restservice.service';

@Injectable({
  providedIn: 'root'
})

@Component({
  selector: 'app-sqlserver',
  templateUrl: './sqlserver.component.html',
  styleUrls: ['./sqlserver.component.css']
})
export class SqlserverComponent implements OnInit {
  @Input() connector: any;
  isworking = true;
  showTab1 = true;
  showTab2: boolean;

  constructor(private restService: RestserviceService) { }
  @Output() onDataChange: EventEmitter<any> = new EventEmitter();

  ngOnInit(): void {
    console.log("SqlserverComponent: ngOnInit:connector:", this.connector);
    this.isworking = false;
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
    // console.log('SqlserverComponent: connectionString');
    this.connector.connectionString = 'jdbc:sqlserver://' + this.connector.hostname + ':' + this.connector.port + ';databaseName=' + this.connector.database + ';user=' + this.connector.username + ';password=' + this.connector.password + ';';
  }

  saveConnection(): void {
    // console.log('SqlserverComponent: saveConnection');
    this.onDataChange.emit("isworking");
    this.restService.postConnector(this.connector)
      .subscribe(data => {
        this.onDataChange.emit(data);
      });
  }
}
