import { NgModule, SecurityContext } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MarkdownModule } from 'ngx-markdown';
import { Help123RoutingModule } from './help123-routing.module';
import { Help123Component } from './help123.component';



@NgModule({
  declarations: [
    Help123Component
  ],
  imports: [
    CommonModule,
    Help123RoutingModule,
    MarkdownModule.forRoot({
      sanitize: SecurityContext.NONE
    }),
    MarkdownModule.forChild()
  ]
})
export class Help123Module { }



@NgModule({
  declarations: [],
  imports: [
    CommonModule,
    Help123RoutingModule,
    MarkdownModule.forRoot({
      sanitize: SecurityContext.NONE
    }),
    MarkdownModule.forChild()
  ]
})
export class MdDemoModule { }
