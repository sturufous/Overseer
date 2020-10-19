import { Component } from '@angular/core';
import { Server, Stopped, ServerListService } from '../app.service';
import * as Highcharts from 'highcharts';

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
  hostInitializer: any;
  interval: any;
  threadImage: string;
  connectionImage: string;
  dataPoint1: any;
  chart1: any;
  hostThreads: any[] = [];
  seriesLength: number = 0;
  //splineColors: string[] = ["#0072bc","#188d0c","#f7941d", "#ed1c24","#7b35b0", "#f7941d","#d2027d", "#2e3192", "#019fdb", "#a0410d", "9e005d", "#00746b"];
  shutdownMsg: Stopped;

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
      layout: 'vertical',
      align: 'right',
      verticalAlign: 'middle'
   },
    exporting: {
        enabled: false
    },
    series: []
};


  constructor(private serverListService: ServerListService) {}

  displayDuration(idx:number) {
    this.serverListService.getConfig(idx)
      .subscribe(
        (data: Server) => this.configs[idx] = { ...data }, // success path
        error => this.error = error // error path
      );
  }

  shutdownServer(idx:number) {
    this.serverListService.stopServer(idx)
      .subscribe(
        (data: Stopped) => this.shutdownMsg = { ...data }, // success path
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
    
    this.chart1 = Highcharts.chart('container1', this.options1);

    Highcharts.setOptions({
      time: {
          useUTC: false
      }});

      this.hostInitializer = setInterval(() => {
        
      if(this.serverListService.configUrls.length > 0) {
        for(var idx=0; idx < this.serverListService.configUrls.length; idx++) {
          this.displayDuration(idx); // api call
          var series = this.chart1.series;
          if(series.length == 0) {
            if(this.configs.length > 0) {
              var seriesIndex1 = 0;
              for(var idx2=0; idx2 < this.configs.length; idx2++) {
                if(this.serverListService.configUrls[idx2].graph) {
debugger;
                  var lineColor = "#" + this.serverListService.configUrls[idx2].lineColor;
                  this.chart1.addSeries({name: this.configs[idx2].instanceName, color: lineColor, data: []});
                  this.serverListService.configUrls[idx2].seriesOffset = seriesIndex1;
                  seriesIndex1++;
                }
              }
            }
          }

          if(this.configs.length > 0) {
            this.dataPoint1 = { x: this.configs[idx].timestamp, y: Number(this.configs[idx].duration) };

            var seriesIdx2 = this.serverListService.configUrls[idx].seriesOffset;
            if(seriesIdx2 > -1) {
              if (this.seriesLength == 1200) {
                debugger;
                //for(var point=this.seriesLength - 1200; point > 0; point--) {
                  series[seriesIdx2].data[0].remove();
                //}
              }
            }

            if(seriesIdx2 > -1) {
              series[seriesIdx2].addPoint(this.dataPoint1);
            }
          }
        }
      }
      if(this.seriesLength < 1200) {
        this.seriesLength++;
      }
    }, 3000);
  }

  restartServer($event: MouseEvent, idx: number) {
    $event.preventDefault();
    if(confirm("Restart server " + this.configs[idx].instanceName + "?")) {
      this.shutdownServer(idx);
    }
  }

   threadImageFile(server:Server) {

    var image: string = null;

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

    if (image == null) {
      image = "0-threads.png";
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