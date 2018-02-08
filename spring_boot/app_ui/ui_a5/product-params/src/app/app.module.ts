import { ProductListService } from './domain/ProductListService';
import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';

import { BsDropdownModule } from 'ngx-bootstrap/dropdown';
import { TooltipModule } from 'ngx-bootstrap/tooltip';
import { ModalModule } from 'ngx-bootstrap/modal';

import { AppComponent } from './app.component';
import { ProductListComponent } from './product-list/product-list.component';
import { ProductDetailsComponent } from './product-details/product-details.component'
import { ParamDetailsComponent } from './param-details/param-details.component'
import { ParamDetailsModalComponent } from './product-details/param-details-modal.component'

@NgModule({
  declarations: [
    AppComponent,
    ProductListComponent,
    ProductDetailsComponent,
    ParamDetailsComponent,
    ParamDetailsModalComponent
  ],
  imports: [
    FormsModule,
    BrowserModule,
    HttpClientModule,
    BsDropdownModule.forRoot(),
    TooltipModule.forRoot(),
    ModalModule.forRoot()    
  ],
  providers: [ ProductListService ],
  bootstrap: [ AppComponent ]
})
export class AppModule { }
