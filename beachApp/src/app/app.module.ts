import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import {CategoryComponent} from './category.component'

import { AppComponent } from './app.component';


@NgModule({
  declarations: [
    AppComponent, 
    CategoryComponent
  ],
  imports: [
    BrowserModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
