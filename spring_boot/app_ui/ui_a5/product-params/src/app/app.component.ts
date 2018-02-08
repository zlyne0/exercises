import {Observable, Subject} from "rxjs";

import { Component, EventEmitter } from '@angular/core';
import { Product } from './domain/Product'

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'app';
    
    changeProductObservable: Subject<Product> = new Subject<Product>();
    
    onSelProd(product: Product) {
        console.log('onSelProd', product)
        this.changeProductObservable.next(product);
    }
}
