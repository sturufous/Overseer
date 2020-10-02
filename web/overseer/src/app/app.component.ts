import { Component } from '@angular/core';
import { ServerListService } from './serverlist/serverlist.service';
import { BrowserModule } from '@angular/platform-browser';
import { ServerListComponent } from './serverlist/serverlist.component';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'],
  providers: [ ServerListService, ServerListComponent ]
})

export class AppComponent {
  title = 'Overseer test';
  error: any;
  interval: any;
  
  constructor(private serverListService: ServerListService) {}
}