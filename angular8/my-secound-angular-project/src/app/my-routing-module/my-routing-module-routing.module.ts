import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { ProductDetailsComponent } from '../product-details/product-details.component';
import { ProductListComponent } from '../product-list/product-list.component';
import { PageNotFoundComponent } from '../page-not-found/page-not-found.component';
import { NotfoundComponent } from '../product-details/notfound/notfound.component';
import { CartComponent } from '../cart/cart.component';
import { ShippingComponent } from '../shipping/shipping.component';

const routes: Routes = [
    { path: '', component: ProductListComponent },
    { path: 'products/notfound', component: NotfoundComponent },
    { path: 'products/:productId', component: ProductDetailsComponent },
    { path: 'cart', component: CartComponent },
    { path: 'shipping', component: ShippingComponent },
    { path: '**', component: PageNotFoundComponent }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class MyRoutingModuleRoutingModule { }
