import { ProductListService } from '../domain/ProductListService';
import { Product } from '../domain/Product';
import { Component, OnInit, Output, EventEmitter } from '@angular/core';

@Component({
  selector: 'app-product-list',
  templateUrl: './product-list.component.html',
  styleUrls: ['./product-list.component.css']
})
export class ProductListComponent implements OnInit {

    @Output() onSelectProduct: EventEmitter<Product> = new EventEmitter();
    
    products : Product[]
    selectedProduct : Product

    constructor(private productListService: ProductListService) {
        this.products = []
    }

    ngOnInit() {
        const self = this;

        this.productListService.getProducts()
            .subscribe(products => self.products = products)
    }
    
    selectProduct(product : Product) {
        console.log('ProductListComponent.selectProduct', product)
        
        this.selectedProduct = product
        this.onSelectProduct.emit(this.selectedProduct);
    } 
}
