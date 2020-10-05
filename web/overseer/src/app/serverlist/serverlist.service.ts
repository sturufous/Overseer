import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';

import { Observable, throwError } from 'rxjs';
import { catchError, retry } from 'rxjs/operators';
import { ActivatedRoute } from '@angular/router';

export interface Server {
  duration: number;
  timestamp: number;
  success: boolean;
  error: string;
  status: string;
  dbProfileName: string;
  instanceName: string;
  instanceRunTime: string;
  startTime: string;
  threadsCompleted: number;
  maxDuration: number;
  minDuration: number;
  buildNumber: string;
  activeThreads: string;
  serverDetails: string;
  connectionStatus: string;
  eventTypesHandled: string;
  serverUser: string;
}

export interface Storage {
  archiveTo: string;
  avHost: string;
  binaryRoot: string;
  captureDir: string;
  ftpRoot: string;
  importFileHours: string;
  maxCdSize: string;
  storageProcessedLoc: string;
  wwwRoot: string;
  ftpHost: string;
  ftpSecure: string;
  ftpType: string;
  ftpUser: string;
}

export interface Host {
  hostIp: string;
  port: string;
}

export interface Mail {
  fromAddress: string;
  toAddress: string;
  hostAddress: string;
  portNumber: string;
}

@Injectable()
export class ServerListService {
  configUrls: string[] = [
    'host=142.36.15.205&port=1089',
    'host=142.36.95.20&port=1089',
    'host=142.36.15.53&port=1089',
    'host=142.36.15.138&port=1089'
    ]

  constructor(private http: HttpClient, private route: ActivatedRoute) { }

  getHosts() {
    debugger;
    var hostList = fetch('http://localhost:8080/hosts').then(function(response) {
            return response.json()
            }).then(function(data) {
              debugger;
                var a = data;
            })
  }

  getConfig(idx:number) {
    return this.http.get<Server>("http://localhost:8080/lastduration?" + this.configUrls[idx])
      .pipe(
        retry(3), // retry a failed request up to 3 times
        catchError(this.handleError) // then handle the error
      );
  }

  getStorage(idx:number) {
    return this.http.get<Storage>("http://localhost:8080/storage?" + this.configUrls[idx])
      .pipe(
        retry(3), // retry a failed request up to 3 times
        catchError(this.handleError) // then handle the error
      );
  }

  getMail(idx:number) {
    return this.http.get<Mail>("http://localhost:8080/mail?" + this.configUrls[idx])
      .pipe(
        retry(3), // retry a failed request up to 3 times
        catchError(this.handleError) // then handle the error
      );
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

  getServer(idx:string) {
    console.log(idx);
    return idx;
  }

}