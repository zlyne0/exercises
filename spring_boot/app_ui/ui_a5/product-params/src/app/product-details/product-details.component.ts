import { ProductParamType } from '../domain/ProductParamType'
import { Product } from '../domain/Product';
import { ProductParam } from '../domain/ProductParam'
import { ProductListService } from '../domain/ProductListService';
import { Observable, Subscription, Subject } from 'rxjs';
 
import { Component, TemplateRef, OnInit, Output, EventEmitter, Input, OnDestroy } from '@angular/core';

@Component({
    selector: 'product-details',
    templateUrl: './product-details.component.html',
    styleUrls: ['./product-details.component.css']
})
export class ProductDetailsComponent implements OnInit, OnDestroy {

    @Input() changeProductListener: Observable<Product>
    
    openModalListener: Subject<ProductParam> = new Subject<ProductParam>()

    private subscriptions: Subscription 
    
    productParameters : ProductParam[]
    product : Product

    selectedParam : ProductParam
    
    constructor( private productListService: ProductListService) {
    }
    
    ngOnInit() {
        this.subscriptions = this.changeProductListener.subscribe(product => {
            this.product = product
            this.fetchProductParmeters(product)
        });
    }

    private fetchProductParmeters(product: Product) {
        this.subscriptions.add(this.productListService.getProductParameters(product)
            .subscribe(params => {
                this.productParameters = params;
            })
        );
    }
    
    ngOnDestroy() {
        this.subscriptions.unsubscribe()
    }

    selectParam(param : ProductParam) {
        this.selectedParam = param
    }

    addParamDialog() {
        let p = new ProductParam()
        p.productId = this.product.id
        
        this.openModalListener.next(p);
    }

    onAddParam(addedProductParam : ProductParam) {
        console.log('saveProductParam ', addedProductParam)
        this.productListService.createProductParam(addedProductParam)
            .subscribe(p => {
                this.fetchProductParmeters(this.product)
            });
    }
    
}