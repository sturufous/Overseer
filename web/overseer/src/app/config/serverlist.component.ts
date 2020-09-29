import { Component } from '@angular/core';
import { Server, ServerListService } from '../serverlist/serverlist.service';

@Component({
  selector: 'app-config',
  templateUrl: './config.component.html',
  providers: [ ServerListService ],
  styles: ['.error {color: red;}']
})

export class ServerDetailsComponent {
  error: any;
  headers: string[];
  config: Server;
  configs: Server[] = [];
  interval: any;
  threadImage: string;
  connectionImage: string;

  constructor(private configService: ServerListService) {}

  displayDuration(idx:number) {
    this.configService.getConfig(idx)
      .subscribe(
        (data: Server) => this.configs[idx] = { ...data }, // success path
        error => this.error = error // error path
      );
  }

  clear() {
    this.configs = undefined;
    this.error = undefined;
    this.headers = undefined;
  }

  showConfig() {
    this.configService.getConfig(0)
      .subscribe(
        (data: Server) => this.config = { ...data }, // success path
        error => this.error = error // error path
      );
  }

  showConfig_v2() {
    this.configService.getConfig(0)
      // clone the data object, using its known Config shape
      .subscribe((data: Server) => this.config = { ...data });
  }

  showConfigResponse() {
    this.configService.getConfigResponse()
      // resp is of type `HttpResponse<Config>`
      .subscribe(resp => {
        // display its headers
        const keys = resp.headers.keys();
        this.headers = keys.map(key =>
          `${key}: ${resp.headers.get(key)}`);

      });
  }
  makeError() {
    this.configService.makeIntentionalError().subscribe(null, error => this.error = error );
  }

  ngOnInit() {
    this.interval = setInterval(() => {
      this.displayDuration(0); // api call
    }, 3000);

    this.interval = setInterval(() => {
      this.displayDuration(1); // api call
    }, 3000);

    this.interval = setInterval(() => {
      this.displayDuration(2); // api call
    }, 3000);
  }

  threadImageFile(config:Server) {

    var image: string;

    if (config.activeThreads == "0") {
      image = "0-threads.png";
    }

    if (config.activeThreads == "1") {
      image = "1-thread.png";
    }

    if (config.activeThreads == "2") {
      image = "2-threads.png";
    }

    if (config.activeThreads == "3") {
      image = "3-threads.png";
    }

    return image;
  }

  connectionImageFile(config:Server) {

    var image: string;

    if (config.connectionStatus == "ONLINE") {
      image = "connected.png";
    } else {
      image = "disconnected.png";
    }

    return image;
  }

  ngOnDestroy() {
    if (this.interval) {
      clearInterval(this.interval);
    }
 }

}