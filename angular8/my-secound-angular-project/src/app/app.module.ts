import { BrowserModule } from '@angular/platform-browser';
import { NgModule, LOCALE_ID, OnInit } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { registerLocaleData } from '@angular/common';
import localePl from '@angular/common/locales/pl';

import { AppComponent } from './app.component';
import { ProductAlertsComponent } from  './product-alerts/product-alerts.component';
import { ProductDetailsComponent } from './product-details/product-details.component';
import { ProductListComponent } from './product-list/product-list.component';
import { PageNotFoundComponent } from './page-not-found/page-not-found.component';
import { MyRoutingModuleRoutingModule } from './my-routing-module/my-routing-module-routing.module';
import { NotfoundComponent } from './product-details/notfound/notfound.component';
import { CartComponent } from './cart/cart.component';
import { ShippingComponent } from './shipping/shipping.component';

registerLocaleData(localePl)

@NgModule({
  declarations: [
    AppComponent, 
    ProductAlertsComponent, 
    ProductDetailsComponent, 
    ProductListComponent, 
    PageNotFoundComponent, 
    NotfoundComponent, 
    CartComponent,
    ShippingComponent
  ],
  imports: [
    HttpClientModule,
    BrowserModule,
    ReactiveFormsModule,
    MyRoutingModuleRoutingModule
  ],
  providers: [
    { 
      provide: LOCALE_ID, useValue: "pl"
    }
  ],
  bootstrap: [AppComponent]
})
export class AppModule implements OnInit {

  ngOnInit() {
  }
}
