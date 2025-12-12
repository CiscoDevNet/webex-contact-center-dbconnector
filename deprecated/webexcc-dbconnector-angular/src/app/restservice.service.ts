import { Injectable, EventEmitter } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';



const httpOptions = {
  // don't set the headers here. set the headers on the server response object

  // headers: new HttpHeaders({
  //   // 'Access-Control-Allow-Origin': '*',
  //   // 'Access-Control-Request-Method': '*',
  //   // 'Access-Control-Allow-Headers': 'X-Requested-With,content-type'
  // }),
  withCredentials: true
};

@Injectable({
  providedIn: 'root'
})
export class RestserviceService {
  public static url = "";

  constructor(private http: HttpClient) {
    console.log('RestserviceService: constructor:');
    const config = require("../assets/env.json");
    RestserviceService.url = config.resourceUrl;

  }
  public static isLoggedIn = false;
  public onLoginChange: EventEmitter<boolean> = new EventEmitter<boolean>();
  public onTokenChange: EventEmitter<any> = new EventEmitter<any>();

  //Deprecated
  public logout() {
    console.log('RestserviceService: mylogout');
    return this.http.get<any[]>(RestserviceService.url + '/mylogout').subscribe(response => {
      RestserviceService.isLoggedIn = false;
      this.onLoginChange.emit(RestserviceService.isLoggedIn);
    });
  }

  //Deprecated
  public getUser() {
    console.log('RestserviceService: getUser');
    return this.http.get<any[]>(RestserviceService.url + '/user', httpOptions).pipe(
      //catchError(this.handleError)
    );
  }

  //Deprecated
  public isAuthenticated() {
    console.log('RestserviceService: isAuthenticated');
    this.getUser().subscribe(data => {
      console.log('restService.getUser:1', data);
      if (data == null) {
        RestserviceService.isLoggedIn = false;
      } else {
        const response: any = data;
        console.log('RestserviceService.getUser: data', data, response);
        if (response === true) {
          console.log('RestserviceService: isAuthenticated: YES');
          RestserviceService.isLoggedIn = true;
        } else {
          console.log('RestserviceService: isAuthenticated: NO');
          RestserviceService.isLoggedIn = false;
        }
      }
      console.log('RestserviceService.getUser: RestserviceService.isLoggedIn:', RestserviceService.isLoggedIn);
      this.onLoginChange.emit(RestserviceService.isLoggedIn);
    });
  }


  public getIsLoggedIn(): boolean {
    console.log('RestserviceService: getIsLoggedIn');
    return RestserviceService.isLoggedIn;
  }

  public getConnector() {
    return this.http.get<any[]>(RestserviceService.url + '/rest/connector', httpOptions).pipe(
      catchError(this.handleError)
    );
  }

  public getConnectorByServerType(serverType: string) {
    return this.http.get<any[]>(RestserviceService.url + '/rest/connector/' + serverType, httpOptions).pipe(
      catchError(this.handleError)
    );
  }

  public postConnector(connector: any) {

    return this.http.post<any[]>(RestserviceService.url + '/rest/connector', connector,).pipe(
      catchError(this.handleError)
    );
  }

  public getBasicAuth() {
    return this.http.get<any[]>(RestserviceService.url + '/rest/basicauth').pipe(
      catchError(this.handleError)
    );
  }

  public getEndpoints() {
    return this.http.get<any[]>(RestserviceService.url + '/rest/endpoints').pipe(
      catchError(this.handleError)
    );
  }

  public executeEndPoint(endpointName: any) {
    return this.http.get<any[]>(RestserviceService.url + '/rest/webexcc/' + endpointName, httpOptions).pipe(
      catchError(this.handleError)
    );
  }

  public saveBasicAuthentication(basicAuth: any) {
    return this.http.post<any[]>(RestserviceService.url + '/rest/basicauth', basicAuth).pipe(
      catchError(this.handleError)
    );
  }

  public saveEndpointAndTest(endpoint: any) {
    return this.http.post<any[]>(RestserviceService.url + '/rest/endpoint', endpoint).pipe(
      catchError(this.handleError)
    );
  }

  public deleteEndpoint(endpoint: any) {
    return this.http.delete<any[]>(RestserviceService.url + '/rest/endpoint/' + endpoint.name).pipe(
      catchError(this.handleError)
    );
  }

  private handleError(error: HttpErrorResponse) {
    console.warn('RestserviceService: private handleError: 1 :', error);
    // if (error.error instanceof ErrorEvent) {
    //   // A client-side or network error occurred. Handle it accordingly.
    //   console.warn('RestserviceService: private handleError: 2:', error);
    // } else {
    //   // The backend returned an unsuccessful response code.
    //   // The response body may contain clues as to what went wrong.
    //   console.warn('RestserviceService: private handleError: 3 :Backend returned code ' + error.status + ' ' + 'body was: ' + error.error);
    //   if (error.status === 0) {
    //     console.warn('RestserviceService: private handleError: 3b redirect to /login');
    //     // location.assign('/oauth2/authorization/dbconnector');
    //   }
    // }
    // Return an observable with a user-facing error message.
    return throwError('{"Exception":" ' + window.btoa('Something bad happened; please try again later.') + '"}');
  }


  login(windowLocationSearch: any) {
    console.log('RestserviceService: login');
    return this.http.get<any>(RestserviceService.url + '/login' + windowLocationSearch, httpOptions).pipe(
      //catchError(this.handleError)
    );
  }

  public getUserV2() {
    console.log('RestserviceService: getUserV2');
    return this.http.get<any>(RestserviceService.url + '/userV2', httpOptions).pipe(
      //catchError(this.handleError)
    );
  }

  public mylogoutV2() {
    console.log('RestserviceService: mylogoutV2');
    return this.http.get<any>(RestserviceService.url + '/mylogoutV2', httpOptions).pipe(
      //catchError(this.handleError)
    );
  }
}
