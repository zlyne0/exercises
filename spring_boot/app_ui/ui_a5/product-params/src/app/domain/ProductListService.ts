import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

import { Product } from './Product'
import { ProductParam } from './ProductParam'
import { ProductParamType } from './ProductParamType'

@Injectable()
export class ProductListService {
    constructor(private http: HttpClient) {

    }

    getProducts() {
        return this.http.get<Product[]>('/rest/product/list');
    }

    getProductParameters(product : Product) {
        return this.http.get<ProductParam[]>('/rest/product/' + product.id + '/parameters')
    }
    
    getProductParamTypeDictionary() {
        return this.http.get<ProductParamType[]>('/rest/parameterType/list');
    }
    
}