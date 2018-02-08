import { ProductParamType } from '../domain/ProductParamType'
import { Product } from '../domain/Product';
import { ProductParam } from '../domain/ProductParam'
import { ProductListService } from '../domain/ProductListService';
import { Observable, Subscription } from 'rxjs';
 
import { Component, TemplateRef, OnInit, Output, EventEmitter, Input, OnDestroy } from '@angular/core';

@Component({
    selector: 'param-details',
    templateUrl: './param-details.component.html',
    styleUrls: ['./param-details.component.css']
})
export class ParamDetailsComponent implements OnInit, OnDestroy {
    private subscriptions: Subscription
    
    productParamsTypes : ProductParamType[]
    @Input() editedParam : ProductParam
    
    constructor( private productListService: ProductListService) {
        this.editedParam = new ProductParam(null, null, null, null, null)
    }
    
    ngOnInit() {
        const self = this;

        this.subscriptions = this.productListService.getProductParamTypeDictionary()
            .subscribe(paramsType => {
                self.productParamsTypes = paramsType;
            });
    }

    ngOnDestroy() {
        this.subscriptions.unsubscribe()
    }

    changeParamType(paramType : ProductParamType) {
        this.editedParam.type = paramType;
    }

}