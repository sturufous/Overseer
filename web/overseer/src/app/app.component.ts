import { Component } from '@angular/core';
import { ServerListService } from './app.service';
import { ServerListComponent } from './serverlist/serverlist.component';
import { Title } from '@angular/platform-browser';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'],
  providers: [ ServerListService, ServerListComponent ]
})

export class AppComponent {
  title = 'Overseer';
  error: any;
  interval: any;
  
  constructor(private serverListService: ServerListService) {}

}