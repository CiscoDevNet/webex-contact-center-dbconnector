import { Component, OnInit } from '@angular/core';
import { RestserviceService } from '../restservice.service';
import { GlobalConstants } from '../global-constants';

@Component({
  selector: 'app-connector',
  templateUrl: './connector.component.html',
  styleUrls: ['./connector.component.css']
})
export class ConnectorComponent implements OnInit {
  isworking = true;
  isLoggedIn: boolean;
  showTab1 = true;
  showTab2: boolean;
  connector: any = {
    type: '',
    version: '',
    hostname: '',
    port: '',
    database: '',
    username: '',
    password: '',
    driver: '',
    connectionString: '',
    connectionPool: {
      initialPoolSize: '',
      minPoolSize: '',
      acquireIncrement: '',
      maxPoolSize: '',
      maxStatements: '',
      unreturnedConnectionTimeout: ''
    }
  };
  connectorMySql: any;
  connectorSqlServer: any;
  myConsole: any = '';

  constructor(private restService: RestserviceService) {
    this.isLoggedIn = GlobalConstants.isLoggedIn;
  }

  ngOnInit() {
    console.log('ConnectorComponent: ngOnInit', this.isLoggedIn);
    this.isLoggedIn = GlobalConstants.isLoggedIn;
    this.getConnector();
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

  getConnector(): void {
    console.log('ConnectorComponent: getConnector');
    this.restService.getConnector()
      .subscribe((data: any) => {
        // console.log('ConnectorComponent: getConnector', data);
        this.connector = data;
        if (this.connector.type === 'MySQL') {
          this.connectorMySql = this.connector;
        } else if (this.connector.type === 'SQL_Server') {
          this.connectorSqlServer = this.connector;
        } else {
          this.myConsole = 'Not a valid connector type';
        }
        // get old connectors so you don't have to retype everything
        if (!this.connectorMySql) {
          this.connectorSqlServer = this.getConnectorByServerType('MySQL');
        }
        if (!this.connectorSqlServer) {
          this.connectorSqlServer = this.getConnectorByServerType('SQL_Server');
        }
        this.isLoggedIn = true;
        this.isworking = false;
      });
  }


  getConnectorByServerType(serverType: string): void {
    console.log('ConnectorComponent: getConnectorByServerType');
    this.restService.getConnectorByServerType(serverType)
      .subscribe((data: any) => {
        // console.log('ConnectorComponent: getConnectorByServerType', data);
        if (serverType === 'MySQL') {
          this.connectorMySql = data;
        } else if (serverType === 'SQL_Server') {
          this.connectorSqlServer = data;
        } else {
          this.myConsole = 'Not a valid connector type';
        }
      });
  }
  onOptionsSelected(event: any): void {
    console.log('ConnectorComponent: onOptionsSelected', event.target.value);
    if (event.target.value === 'MySQL') {
      this.connector = this.connectorMySql;
    } else if (event.target.value === 'SQL_Server') {
      this.connector = this.connectorSqlServer;
    } else {
      this.myConsole = 'Not a valid connector type';
    }
  }

  saveConnection(): void {
    console.log('ConnectorComponent: saveConnection');
    this.isworking = true;
    this.myConsole = '';
    this.restService.postConnector(this.connector)
      .subscribe(data => {
        this.myConsole = data;
        if (this.myConsole.Exception) {
          this.myConsole = 'Exception:\n' + atob(this.myConsole.Exception);
        } else {
          this.myConsole = this.myConsole.response;
        }
        this.isworking = false;
      });
  }

  updateConnectionString(): void {
    console.log('ConnectorComponent: connectionString');
    if (this.connector.type === 'MySQL') {
      // tslint:disable-next-line: max-line-length
      this.connector.connectionString = 'jdbc:mysql://' + this.connector.hostname + ':' + this.connector.port + '/' + this.connector.database + '?serverTimezone=UTC&useLegacyDatetimeCode=false&useSSL=FALSE&user=' + this.connector.username + '&password=' + this.connector.password + '';
    } else if (this.connector.type === 'SQL_Server') {
      // tslint:disable-next-line: max-line-length
      this.connector.connectionString = 'jdbc:sqlserver://' + this.connector.hostname + ':' + this.connector.port + ';databaseName=' + this.connector.database + ';user=' + this.connector.username + ';password=' + this.connector.password + ';';
    } else {
      this.myConsole = 'Not a valid connector type';
    }
  }
}
