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
  
  constructor(private configService: ConfigService) {}

  displayDuration() {
    this.configService.getConfig()
      .subscribe(
        (data: Config) => this.config = { ...data }, // success path
        error => this.error = error // error path
      );
  }

  ngOnInit() {
    this.displayDuration();
  }
}
