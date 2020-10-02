import { BrowserModule, Title } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { HttpClientModule } from '@angular/common/http';
import { CommonModule } from '@angular/common';  
import { ServerListComponent } from './serverlist/serverlist.component';
import { ServerDetailsComponent } from './serverdetails/serverdetails.component';

@NgModule({
  declarations: [
    AppComponent,
    ServerListComponent,
    ServerDetailsComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    CommonModule
  ],
  providers: [ Title ],
  bootstrap: [AppComponent]
})
export class AppModule { }