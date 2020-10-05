import { Component } from '@angular/core';
import { Server, Storage, ServerListService } from '../app.service';

@Component({
  selector: 'app-config',
  templateUrl: './serverlist.component.html',
  providers: [ ServerListService ],
  styles: ['.error {color: red;}']
})

export class ServerListComponent {
  error: any;
  headers: string[];
  config: Server;
  configs: Server[] = [];
  interval: any;
  threadImage: string;
  connectionImage: string;

  constructor(private serverListService: ServerListService) {}

  displayDuration(idx:number) {
    this.serverListService.getConfig(idx)
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

  ngOnInit() {
  
    this.serverListService.loadHosts();

    this.interval = setInterval(() => {
      if(this.serverListService.configUrls.length > 0) {
        this.displayDuration(0); // api call
      }
    }, 3000);

    this.interval = setInterval(() => {
      if(this.serverListService.configUrls.length > 1) {
        this.displayDuration(1); // api call
      }
    }, 3000);

    this.interval = setInterval(() => {
      if(this.serverListService.configUrls.length > 2) {
        this.displayDuration(2); // api call
      }
    }, 3000);

    this.interval = setInterval(() => {
      if(this.serverListService.configUrls.length > 3) {
        this.displayDuration(3); // api call
      }
    }, 3000);
  }

  threadImageFile(server:Server) {

    var image: string;

    if (server.activeThreads == "0") {
      image = "0-threads.png";
    }

    if (server.activeThreads == "1") {
      image = "1-thread.png";
    }

    if (server.activeThreads == "2") {
      image = "2-threads.png";
    }

    if (server.activeThreads == "3") {
      image = "3-threads.png";
    }

    return image;
  }

  connectionImageFile(server:Server) {

    var image: string;

    if (server.connectionStatus == "ONLINE") {
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