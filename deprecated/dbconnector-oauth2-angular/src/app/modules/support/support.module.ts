import { NgModule, SecurityContext } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MarkdownModule } from 'ngx-markdown';
import { SupportRoutingModule } from './support-routing.module';
import { SupportComponent } from './support.component';



@NgModule({
  declarations: [
    SupportComponent
  ],
  imports: [
    CommonModule,
    SupportRoutingModule,
    MarkdownModule.forRoot({
      sanitize: SecurityContext.NONE
    }),
    MarkdownModule.forChild()
  ]
})
export class SupportModule { }



@NgModule({
  declarations: [],
  imports: [
    CommonModule,
    SupportRoutingModule,
    MarkdownModule.forRoot({
      sanitize: SecurityContext.NONE
    }),
    MarkdownModule.forChild()
  ]
})
export class MdDemoModule { }
