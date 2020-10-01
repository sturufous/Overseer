import { Component } from '@angular/core';
import { Server, ServerListService } from '../serverlist/serverlist.service';
import { ActivatedRoute } from '@angular/router';
import * as Highcharts from 'highcharts';

declare var require: any;
let Boost = require('highcharts/modules/boost');
let noData = require('highcharts/modules/no-data-to-display');
let More = require('highcharts/highcharts-more');

Boost(Highcharts);
noData(Highcharts);
More(Highcharts);
noData(Highcharts);

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
  dataPoint: any;
  chart: any;

  public options1:any = {
        
    chart: {
        type: 'spline',
        events: {
        }
    },
    title: {
        text: 'Jorel2 Test Server - Last Thread Duration'
    },
    xAxis: {
        type: 'datetime',
    },
    yAxis: {
        title: {
        text: 'Thread Duration (seconds)'
        }
    },
    legend: {
        enabled: false
    },
    exporting: {
        enabled: false
    },
    series: [{
        name: 'Thread Duration (seconds)',
        data: []
    }]
};

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
    this.chart = Highcharts.chart('container', this.options1);
    this.route.paramMap.subscribe(params => {
	
    this.interval = setInterval(() => {
        this.displayDuration(+params.get('id')); // api call - + converts string to number
        this.dataPoint = { x: this.server.timestamp, y: Number(this.server.duration) };
        //debugger;
        this.chart.series[0].addPoint(this.dataPoint);
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