import { Component } from '@angular/core';
import { Server, ServerListService } from '../serverlist/serverlist.service';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-serverdetails',
  templateUrl: './serverdetails.component.html',
  providers: [ ServerListService ],
  styles: ['.error {color: red;}']
})

export class ServerDetailsComponent {
  error: any;
  headers: string[];
  server: Server;
  interval: any;
  threadImage: string;
  connectionImage: string;

  constructor(private serverListService: ServerListService, private route: ActivatedRoute) {}

  displayDuration(idx:number) {
    this.serverListService.getConfig(idx)
      .subscribe(
        (data: Server) => this.server = { ...data }, // success path
        error => this.error = error // error path
      );
  }

  clear() {
    this.server = undefined;
    this.error = undefined;
    this.headers = undefined;
  }

  ngOnInit() {
    this.route.paramMap.subscribe(params => {
	
    this.interval = setInterval(() => {
        this.displayDuration(+params.get('id')); // api call - + converts string to number
        }, 3000);
    })   
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