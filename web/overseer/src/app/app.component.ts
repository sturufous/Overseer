import { Component } from '@angular/core';
import { Config, ConfigService } from './config/config.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'],
  providers: [ ConfigService ]
})

export class AppComponent {
  title = 'Overseer';
  config: Config;
  configs: Config[] = [];
  error: any;
  threadImage: string;
  connectionImage: string;
  interval: any;
  
  constructor(private configService: ConfigService) {}

  displayDuration(idx:number) {
    this.configService.getConfig(idx)
      .subscribe(
        (data: Config) => this.configs[idx] = { ...data }, // success path
        error => this.error = error // error path
      );
  }

  ngOnInit() {
    this.interval = setInterval(() => {
      this.displayDuration(0); // api call
   }, 3000);

   this.interval = setInterval(() => {
    this.displayDuration(1); // api call
  }, 3000);

  }

  threadImageFile(config:Config) {

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

  connectionImageFile(config:Config) {

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