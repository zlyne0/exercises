import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import 'rxjs/add/operator/map';
import { Observable } from 'rxjs';

import { Product } from './Product'
import { ProductParam } from './ProductParam'
import { ProductParamType } from './ProductParamType'

@Injectable()
export class ProductListService {
    constructor(private http: HttpClient) {

    }

    getProducts() : Observable<Product[]> {
        return this.http.get<Product[]>('/rest/product/list')
            .map(res => {
                let products : Array<Product> = new Array<Product>()
                for (let i=0; i<res.length; i++) {
                    products.push(new Product(res[i].name, res[i].id))
                } 
                return products;
            });
    }

    getProductParameters(product : Product) {
        return this.http.get<ProductParam[]>(`/rest/product/${product.id}/parameters`)
    }
    
    getProductParamTypeDictionary() {
        return this.http.get<ProductParamType[]>('/rest/parameterType/list');
    }
    
    createProductParam(param : ProductParam) : Observable<ProductParam> {
        return this.http.post<ProductParam>(`/rest/product/${param.productId}/params`, param)
            .map(item => {
                return new ProductParam(
                    item.id, item.productId, 
                    item.value, item.bigValue,
                    new ProductParamType(item.type.id, item.type.name)
                );
            });
    }
    
}