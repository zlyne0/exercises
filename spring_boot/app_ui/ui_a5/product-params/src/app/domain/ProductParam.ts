import { ProductParamType } from './ProductParamType'

export class ProductParam {
    constructor(
        public id: Number, 
        public productId: Number,
        public value: String,
        public bigValue : String,
        public type : ProductParamType
    ) {
    }
}