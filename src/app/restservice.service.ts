import { Injectable, EventEmitter } from '@angular/core';
import { HttpClient, HttpErrorResponse, HttpHeaders } from '@angular/common/http';
// import { HttpClient, , HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError, Subject } from 'rxjs';
import { catchError, retry } from 'rxjs/operators';

// import { Location } from '@angular/common';
// import { Router } from '@angular/router';

// const httpOptions = {
//   headers: new HttpHeaders({
//     'Set-Cookie': 'SameSite=Strict; Secure',
//   })
// };

@Injectable({
  providedIn: 'root'
})
export class RestserviceService {

  constructor(private http: HttpClient) { }
  public static isLoggedIn = false;
  public onLoginChange: EventEmitter<boolean> = new EventEmitter<boolean>();


  public getUser() {
    console.log('RestserviceService: getUser');
    return this.http.get<any[]>('/user/user').pipe(
      catchError(this.handleError)
    );
  }

  public isAuthenticated() {
    console.log('RestserviceService: isAuthenticated');
    this.getUser().subscribe(data => {
      // console.warn('restService.getUser:1', data);
      if (data == null) {
        RestserviceService.isLoggedIn = false;
      } else {
        const response: any = data;
        console.warn('restService.getUser: data', data);
        if (response.response === 'true') {
          RestserviceService.isLoggedIn = true;

        } else {
          RestserviceService.isLoggedIn = false;
        }
      }
      this.onLoginChange.emit(RestserviceService.isLoggedIn);
    });
  }

  public getIsLoggedIn(): boolean {
    console.log('RestserviceService: getIsLoggedIn');
    return RestserviceService.isLoggedIn;
  }


  public getConnector() {
    return this.http.get<any[]>('/rest/connector').pipe(
      catchError(this.handleError)
    );
  }
  public getConnectorByServerType(serverType: string) {
    return this.http.get<any[]>('/rest/connector/' + serverType).pipe(
      catchError(this.handleError)
    );
  }
  public postConnector(connector: any) {
    return this.http.post<any[]>('/rest/connector', connector).pipe(
      catchError(this.handleError)
    );
  }

  public getBasicAuth() {
    return this.http.get<any[]>('/rest/basicauth').pipe(
      catchError(this.handleError)
    );
  }

  public getEndpoints() {
    return this.http.get<any[]>('/rest/endpoints').pipe(
      catchError(this.handleError)
    );
  }


  public executeEndPoint(endpointName: any) {
    return this.http.get<any[]>('/rest/webexcc/' + endpointName).pipe(
      catchError(this.handleError)
    );
  }

  public saveBasicAuthentication(basicAuth: any) {
    return this.http.post<any[]>('/rest/basicauth', basicAuth).pipe(
      catchError(this.handleError)
    );
  }

  public saveEndpointAndTest(endpoint: any) {
    return this.http.post<any[]>('/rest/endpoint', endpoint).pipe(
      catchError(this.handleError)
    );
  }

  public deleteEndpoint(endpoint: any) {
    return this.http.delete<any[]>('/rest/endpoint/' + endpoint.name).pipe(
      catchError(this.handleError)
    );
  }

  private handleError(error: HttpErrorResponse) {
    console.warn('RestserviceService: private handleError: 1 :', error);
    if (error.error instanceof ErrorEvent) {
      // A client-side or network error occurred. Handle it accordingly.
      console.warn('RestserviceService: private handleError: 2:', error);
    } else {
      // The backend returned an unsuccessful response code.
      // The response body may contain clues as to what went wrong.
      console.warn('RestserviceService: private handleError: 3 :Backend returned code ' + error.status + ' ' + 'body was: ' + error.error);
      if (error.status === 0) {
        console.warn('RestserviceService: private handleError: 3b redirect to /login');
        location.assign('/oauth2/authorization/dbconnector');
      }
    }
    // Return an observable with a user-facing error message.
    return throwError('{"Exception":" ' + window.btoa('Something bad happened; please try again later.') + '"}');
  }


}
