import { Injectable, Component, OnInit, Input, Output, EventEmitter } from '@angular/core';
import { RestserviceService } from '../../restservice.service';

@Injectable({
  providedIn: 'root'
})

@Component({
  selector: 'app-mysql',
  templateUrl: './mysql.component.html',
  styleUrls: ['./mysql.component.css']
})

export class MysqlComponent implements OnInit {
  @Input() connector: any;
  isworking = true;
  showTab1 = true;
  showTab2: boolean;

  constructor(private restService: RestserviceService) { }
  @Output() onDataChange: EventEmitter<any> = new EventEmitter();

  ngOnInit(): void {
    console.log("MysqlComponent: ngOnInit:connector:", this.connector);
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
    // console.log('MysqlComponent: connectionString');
    this.connector.connectionString = 'jdbc:mysql://' + this.connector.hostname + ':' + this.connector.port + '/' + this.connector.database + '?serverTimezone=UTC&useLegacyDatetimeCode=false&useSSL=FALSE&user=' + this.connector.username + '&password=' + this.connector.password + '';
  }

  saveConnection(): void {
    // console.log('MysqlComponent: saveConnection');
    this.onDataChange.emit("isworking");
    this.restService.postConnector(this.connector)
      .subscribe(data => {
        this.onDataChange.emit(data);
      });
  }
}
