import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { ServerListComponent } from './serverlist/serverlist.component';
import { ServerDetailsComponent } from './serverdetails/serverdetails.component';

const routes: Routes = [
  { path: '', pathMatch: 'full', redirectTo: 'serverlist' },
  { path: 'serverlist', component: ServerListComponent },
  { path: 'serverdetails/:id', component: ServerDetailsComponent }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }