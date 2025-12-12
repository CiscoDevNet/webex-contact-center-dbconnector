import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { Help123Component } from './help123.component';

const routes: Routes = [{ path: '', component: Help123Component }];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],

})
export class Help123RoutingModule { }
