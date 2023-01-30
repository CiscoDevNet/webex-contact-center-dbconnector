import { NgModule, SecurityContext } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AboutComponent } from './about.component';
import { MarkdownModule } from 'ngx-markdown';
import { AboutRoutingModule } from './about-routing.module';



@NgModule({
  declarations: [
    AboutComponent
  ],
  imports: [
    CommonModule,
    AboutRoutingModule,
    MarkdownModule.forRoot({
      sanitize: SecurityContext.NONE
    }),
    MarkdownModule.forChild()
  ]
})
export class AboutModule { }



@NgModule({
  declarations: [],
  imports: [
    CommonModule,
    AboutRoutingModule,
    MarkdownModule.forRoot({
      sanitize: SecurityContext.NONE
    }),
    MarkdownModule.forChild()
  ]
})
export class MdDemoModule { }
