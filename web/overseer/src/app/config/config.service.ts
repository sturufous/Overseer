import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';

import { Observable, throwError } from 'rxjs';
import { catchError, retry } from 'rxjs/operators';

export interface Config {
  duration: number;
  timestamp: number;
  success: boolean;
  error: string;
  status: string;
  dbProfileName: string;
  instanceName: string;
  runTime: string;
  startTime: string;
  threadsCompleted: number;
  maxDuration: number;
  minDuration: number;
  buildNumber: string;
  activeThreads: string;
  serverDetails: string;
  connectionStatus: string;
  eventTypesHandled: string;
}

@Injectable()
export class ConfigService {
  configUrl = 'http://localhost:8080/lastduration?host=142.36.95.20&port=1089';

  configUrls: string[] = [
    'http://localhost:8080/lastduration?host=142.36.15.205&port=1089',
    'http://localhost:8080/lastduration?host=142.36.95.20&port=1089'
    ]

  constructor(private http: HttpClient) { }

  getConfig(idx:number) {
    return this.http.get<Config>(this.configUrls[idx])
      .pipe(
        retry(3), // retry a failed request up to 3 times
        catchError(this.handleError) // then handle the error
      );
  }

  getConfig_1() {
    return this.http.get(this.configUrl);
  }

  getConfig_2() {
    // now returns an Observable of Config
    return this.http.get<Config>(this.configUrl);
  }

  getConfig_3() {
    return this.http.get<Config>(this.configUrl)
      .pipe(
        catchError(this.handleError)
      );
  }

  getConfigResponse(): Observable<HttpResponse<Config>> {
    return this.http.get<Config>(
      this.configUrl, { observe: 'response' });
  }

  private handleError(error: HttpErrorResponse) {
    if (error.error instanceof ErrorEvent) {
      // A client-side or network error occurred. Handle it accordingly.
      console.error('An error occurred:', error.error.message);
    } else {
      // The backend returned an unsuccessful response code.
      // The response body may contain clues as to what went wrong.
      console.error(
        `Backend returned code ${error.status}, ` +
        `body was: ${error.error}`);
    }
    // Return an observable with a user-facing error message.
    return throwError(
      'Something bad happened; please try again later.');
  }

  makeIntentionalError() {
    return this.http.get('not/a/real/url')
      .pipe(
        catchError(this.handleError)
      );
  }

}