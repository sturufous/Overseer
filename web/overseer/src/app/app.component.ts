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
  error: any;
  threadImage: string;
  connectionImage: string;
  interval: any;
  
  constructor(private configService: ConfigService) {}

  displayDuration() {
    this.configService.getConfig()
      .subscribe(
        (data: Config) => this.config = { ...data }, // success path
        error => this.error = error // error path
      );
  }

  ngOnInit() {
    this.threadImage = "0-threads.png";
    this.connectionImage = "connected.png";
    
    this.interval = setInterval(() => {
      this.displayDuration(); // api call

      if (this.config.activeThreads == "0") {
        this.threadImage = "0-threads.png";
      }

      if (this.config.activeThreads == "1") {
        this.threadImage = "1-thread.png";
      }

      if (this.config.activeThreads == "2") {
        this.threadImage = "2-threads.png";
      }

      if (this.config.activeThreads == "3") {
        this.threadImage = "3-threads.png";
      }

      if (this.config.connectionStatus == "ONLINE") {
        this.connectionImage = "connected.png";
      } else {
        this.connectionImage = "disconnected.png";
      }

   }, 3000);
  }

  ngOnDestroy() {
    if (this.interval) {
      clearInterval(this.interval);
    }
 }
}
