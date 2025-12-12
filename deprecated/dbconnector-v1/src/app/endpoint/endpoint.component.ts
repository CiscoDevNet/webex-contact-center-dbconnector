import { Component, OnInit } from '@angular/core';
import { RestserviceService } from '../restservice.service';
import { GlobalConstants } from '../global-constants';

@Component({
  selector: 'app-endpoint',
  templateUrl: './endpoint.component.html',
  styleUrls: ['./endpoint.component.css']
})
export class EndpointComponent implements OnInit {
  isworking = true;
  isLoggedIn: boolean;
  showTab1 = true;
  showTab2: boolean;
  endpoints: any = [];
  endpoint: any = {};
  basicAuth: any = {};
  myConsole: any = '';
  base64ValuePlaceholder: any = '';

  constructor(private restService: RestserviceService) {
    this.isLoggedIn = GlobalConstants.isLoggedIn;
  }

  ngOnInit() {
    console.log('EndpointComponent: ngOnInit', this.isLoggedIn);
    this.isLoggedIn = GlobalConstants.isLoggedIn;
    this.myConsole = '';
    this.getBasicAuth();
    this.getEndpoints();
  }

  showServerTabButton(tab: string): void {
    console.log('EndpointComponent: showServerTabButton');
    this.myConsole = '';
    this.showTab1 = false;
    this.showTab2 = false;

    if (tab === 'tab1') {
      this.showTab1 = true;
    }
    if (tab === 'tab2') {
      this.showTab2 = true;
    }
  }

  addEndpoint(): void {
    console.log('EndpointComponent: addEndpoint');
    this.myConsole = '';
    this.showServerTabButton('tab1');
    const newEndpoint: any = {};
    newEndpoint.name = this.uuidv4();
    newEndpoint.endpoint = '/rest/webexcc/' + newEndpoint.name;
    newEndpoint.nameValueList = [];
    this.endpoints.push(newEndpoint);
    this.endpoint = newEndpoint;
    this.endpoint.query = 'select ${ani}';
    this.addNameValuePair({ name: 'ani', value: '1234567890' });
    this.highlightEndpoint();
  }

  deleteEndpoint(index: any): void {
    console.log('EndpointComponent: deleteEndpoint:', index);
    const answer = window.confirm('Are sure you want to delete this item ?');
    if (answer) {
      this.isworking = true;
      this.myConsole = '';
      this.restService.deleteEndpoint(this.endpoints[index])
        .subscribe(data => {
          this.basicAuth = data;
          if (this.basicAuth.Exception) {
            this.myConsole = atob(this.myConsole.Exception);
          } else {
            this.myConsole = 'Basic authentication was loaded.\n';
          }
          this.endpoints.splice(index, 1);
          this.isworking = false;
        });
    } else {
      console.log('EndpointComponent: deleteEndpoint: NOT');
    }
  }


  addNameValuePair(nameValue: any): void {
    console.log('EndpointComponent: addNameValue:');
    this.myConsole = '';
    this.endpoint.nameValueList.push(nameValue);
  }

  deleteNameValuePair(index: any): void {
    console.log('EndpointComponent: deleteNameValuePair:');
    const answer = window.confirm('Are sure you want to delete this item ?');
    if (answer) {
      this.myConsole = '';
      this.endpoint.nameValueList.splice(index, 1);
      this.myConsole = 'Name value pair deleted';
    } else {
      console.log('EndpointComponent: deleteNameValuePair: NOT');
    }
  }

  getBasicAuth(): void {
    console.log('EndpointComponent: getBasicAuth');
    this.myConsole = '';
    this.restService.getBasicAuth()
      .subscribe(data => {
        this.basicAuth = data;
        if (this.basicAuth.Exception) {
          this.myConsole = atob(this.myConsole.Exception);
        } else {
          this.myConsole = 'Basic authentication was loaded.\n';
        }
        this.updateBasicAuthValue();
      });
  }

  getEndpoints(): void {
    console.log('EndpointComponent: getEndpoints');
    this.myConsole = '';
    this.restService.getEndpoints()
      .subscribe(data => {
        this.endpoints = data;
        if (this.endpoints.Exception) {
          this.myConsole = atob(this.myConsole.Exception);
        } else {
          this.myConsole += 'Endpoints were loaded.\n';
        }
        if (this.endpoints[0] != null) {
          this.endpoint = this.endpoints[0];
          this.endpoint.isHighlighted = true;
        }
        this.isLoggedIn = true;
        this.isworking = false;
      });
  }


  uuidv4(): string {
    return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function (c) {
      const r = Math.random() * 16 | 0, v = c == 'x' ? r : (r & 0x3 | 0x8);
      return v.toString(16);
    });
  }


  saveBasicAuthentication(): void {
    console.log('EndpointComponent: saveBasicAuthentication');
    this.isworking = true;
    this.myConsole = '';
    this.restService.saveBasicAuthentication(this.basicAuth)
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

  isNameValuePairValid(): boolean {
    let check = true;
    this.endpoint.nameValueList.forEach((nameValue) => {
      if (nameValue.name == null || nameValue.name === undefined || nameValue.name.length < 1) {
        alert('Invalid name value pair:\n Name can\'t be blank.');
        check = false;
      } else if (!isNaN(nameValue.name)) {
        alert('Invalid name value pair:\nName can\'t be a number.');
        check = false;
      } else if (/^[0-9-]/.test(nameValue.name)) {
        alert('Invalid name value pair:\nName can\'t start with a number or dash');
        check = false;
      } else if (/[ !@#$%^&*()+\=\[\]{};':"\\|,.<>\/?]/g.test(nameValue.name)) {
        alert('Invalid name value pair:\nName can only contain the following characters be a-z A-Z 0-9.');
        check = false;
      }
    });
    return check;
  }


  saveEndpointAndTest(): void {
    console.log('EndpointComponent: saveEndpointAndTest');
    this.myConsole = '';
    this.endpoint.isHighlighted = undefined;
    if (!this.isNameValuePairValid()) {
      return;
    }
    this.isworking = true;

    this.restService.saveEndpointAndTest(this.endpoint)
      .subscribe(data => {
        this.myConsole = data;
        if (this.myConsole.Exception) {
          this.myConsole = 'Exception:\n' + atob(this.myConsole.Exception);
        } else {
          const response = this.myConsole.response;
          const params = atob(this.myConsole.httpParams);
          const sqlStatement = atob(this.myConsole.sqlStatement);
          const authentication = this.myConsole.authentication;
          const jsonResponse = atob(this.myConsole.jsonResponse);
          this.myConsole = response + '\n';
          this.myConsole += 'Endpoint authentication is active:' + authentication + '\n';
          this.myConsole += 'Request:\n';
          this.myConsole += location.origin + this.endpoint.endpoint + params + '\n';
          this.myConsole += 'Response:\n';
          this.myConsole += jsonResponse + '\n';
          this.myConsole += 'SQL:\n';
          this.myConsole += sqlStatement + '\n';
        }
        this.endpoint.isHighlighted = true;
        this.isworking = false;
      });
  }

  updateBasicAuthValue(): void {
    console.log('EndpointComponent: basicAuthValue');
    this.myConsole = '';
    if (this.basicAuth.username.length < 12 || this.basicAuth.password.length < 12) {
      this.base64ValuePlaceholder = 'username and password must be 12 charactors';
      this.basicAuth.value = '';
    } else {
      this.basicAuth.value = btoa(this.basicAuth.username + ':' + this.basicAuth.password);
    }
  }

  selectEndpoint(endpoint: any): void {
    console.log('EndpointComponent: selectEndpoint');
    this.myConsole = '';
    this.endpoint = endpoint;
    this.highlightEndpoint();
  }

  setBasicAuthenticationRequired(event: any): void {
    console.log('EndpointComponent: setBasicAuthenticationRequired');
    this.saveBasicAuthentication();
  }

  highlightEndpoint(): void {
    console.log('EndpointComponent: highlightEndpoint');
    this.endpoints.forEach((endpoint: any) => {
      // console.warn('endpoint:', endpoint);
      endpoint.isHighlighted = false;
    });
    this.endpoint.isHighlighted = true;
  }


}
