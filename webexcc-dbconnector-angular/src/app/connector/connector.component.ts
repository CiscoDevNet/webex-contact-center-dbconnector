import { Component, OnInit } from '@angular/core';
import { RestserviceService } from '../restservice.service';


@Component({
  selector: 'app-connector',
  templateUrl: './connector.component.html',
  styleUrls: ['./connector.component.css']
})
export class ConnectorComponent implements OnInit {


  public isLoggedIn: boolean = false;
  public isworking: boolean = true;
  public myToken: any;

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
  connectorOracle: any;
  myConsole: any = '';

  constructor(private restService: RestserviceService) {
    console.log('ConnectorComponent : constructor');
    this.addonTokenChangeListener();
  }

  ngOnInit() {
    console.log('ConnectorComponent: ngOnInit:');
    // get all connectors so you switch between connectors and you don't have to retype everything
    this.connectorMySql = this.getConnectorByServerType('MySQL');
    this.connectorSqlServer = this.getConnectorByServerType('SQL_Server');
    this.connectorOracle = this.getConnectorByServerType('Oracle');
    // get current connector
    this.getConnector();

  }

  onOptionsSelected(event: any): void {
    console.log('ConnectorComponent: onOptionsSelected', event.target.value);

  }

  getConnector(): void {
    console.log('ConnectorComponent: getConnector');
    this.restService.getConnector()
      .subscribe((data: any) => {
        - console.log('ConnectorComponent: getConnector', data);
        this.connector = data;
        if (this.connector.type === 'MySQL') {
          this.connectorMySql = this.connector;
        } else if (this.connector.type === 'SQL_Server') {
          this.connectorSqlServer = this.connector;
        } else if (this.connector.type === 'Oracle') {
          this.connectorOracle = this.connector;
        } else {
          this.myConsole = 'Not a valid connector type';
        }
        this.isworking = false;
      });
  }

  getConnectorByServerType(serverType: string): void {
    console.log('ConnectorComponent: getConnectorByServerType', serverType);
    this.restService.getConnectorByServerType(serverType)
      .subscribe((data: any) => {
        console.log('ConnectorComponent: getConnectorByServerType', data);
        if (serverType === 'MySQL') {
          this.connectorMySql = data;
        } else if (serverType === 'SQL_Server') {
          this.connectorSqlServer = data;
        } else if (serverType === 'Oracle') {
          this.connectorOracle = data;
        } else {
          this.myConsole = 'Not a valid connector type';
        }
      });
  }

  onDataChange(data: any) {
    console.log('ConnectorComponent: MysqlComponent: onDataChange', data);
    if (data === 'isworking') {
      this.myConsole = "";
      this.isworking = true;
    }
    else {
      this.myConsole = data;
      if (this.myConsole.Exception) {
        this.myConsole = 'Exception:\n' + atob(this.myConsole.Exception);
      } else {
        this.myConsole = this.myConsole.response;
      }
      this.isworking = false;
    }
  }

  addonTokenChangeListener() {
    console.log('ConnectorComponent addonTokenChangeListener:');
    this.restService.onTokenChange.subscribe({
      next: (event: any) => {
        console.log('ConnectorComponent addonTokenChangeListener: Received message  ', event);
        if (event.access_token != '') {
          this.isLoggedIn = true;
          return;
        }
        this.isLoggedIn = false;
      }
    });
  }


}
