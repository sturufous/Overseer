import { Component } from '@angular/core';
import { Server, Storage, Mail, ServerListService } from '../app.service';
import { ActivatedRoute } from '@angular/router';
import * as Highcharts from 'highcharts';
import { areAllEquivalent } from '@angular/compiler/src/output/output_ast';
import { Title } from '@angular/platform-browser';

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
  storage: Storage;
  mail: Mail;
  interval: any;
  threadImage: string;
  connectionImage: string;
  dataPoint1: any;
  dataPoint2: any;
  chart1: any;
  chart2: any;
  hideSd: boolean = true;
  hideSt: boolean = true;
  hideMl: boolean = true;
  hostId: number;
  public options1:any = {
        
    chart: {
        type: 'spline',
        backgroundColor: '#fafafa',
        events: {
        }
    },
    title: {
        text: 'Last Thread Duration',
        style: {
          fontFamily: 'Helvetica, Ariel, SansSerif',
          fontWeight: "bold"
        }
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
        data: [],
        color: '#0072bc'
    }]
};

public options2:any = {
        
  chart: {
      type: 'spline',
      backgroundColor: '#fafafa',
      events: {
      }
  },
  title: {
      text: 'Active Thread Count',
      style: {
        fontFamily: 'Helvetica, Ariel, SansSerif',
        fontWeight: "bold"
      }
  },
  xAxis: {
      type: 'datetime',
  },
  yAxis: {
      title: {
      text: 'Active Thread Count'
      }
  },
  legend: {
      enabled: false
  },
  exporting: {
      enabled: false
  },
  series: [{
      name: 'Active Thread Count',
      data: [],
      color: 'green'
  }]
};

  constructor(private serverListService: ServerListService, private route: ActivatedRoute, private titleService: Title) {}

  public setTitle( newTitle: string) {
    this.titleService.setTitle( newTitle );
  }

  displayDuration(idx:number) {
    this.serverListService.getConfig(idx)
      .subscribe(
        (data: Server) => this.server = { ...data }, // success path
        error => this.error = error // error path
      );
  }

  displayStorage(idx:number) {
    this.serverListService.getStorage(idx)
      .subscribe(
        (data: Storage) => this.storage = { ...data }, // success path
        error => this.error = error // error path
      );
  }

  displayMail(idx:number) {
    this.serverListService.getMail(idx)
      .subscribe(
        (data: Mail) => this.mail = { ...data }, // success path
        error => this.error = error // error path
      );
  }

  clear() {
    this.server = undefined;
    this.error = undefined;
    this.headers = undefined;
  }

  ngOnInit() {
    this.serverListService.loadHosts();

    this.chart1 = Highcharts.chart('container1', this.options1);
    this.chart2 = Highcharts.chart('container2', this.options2);

    Highcharts.setOptions({
      time: {
          useUTC: false
      }});

    this.route.paramMap.subscribe(params => {
  
    this.hostId = +params.get('id');

    this.interval = setInterval(() => {
        this.displayDuration(+params.get('id')); // api call - + converts string to number
        this.dataPoint1 = { x: this.server.timestamp, y: Number(this.server.duration) };
        this.dataPoint2 = { x: this.server.timestamp, y: Number(this.server.activeThreads) };

        var series = this.chart1.series[0];
        if (series.data.length > 1200) {
            series.data[0].remove(false, false)
        }

        var series = this.chart2.series[0];
        if (series.data.length > 1200) {
            series.data[0].remove(false, false)
        }

        this.chart1.series[0].addPoint(this.dataPoint1);
        this.chart2.series[0].addPoint(this.dataPoint2);
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

 ngAfterViewInit() {
  this.setTitle(this.server.instanceName);
 }

  toggleSd() {
      this.hideSd = !this.hideSd;
  }

  toggleSt() {
    if(this.hideSt) {
      this.displayStorage(this.hostId);
    }
    this.hideSt = !this.hideSt;
  }

  toggleMl() {
    if(this.hideMl) {
      this.displayMail(this.hostId);
    }
    this.hideMl = !this.hideMl;
  }

}