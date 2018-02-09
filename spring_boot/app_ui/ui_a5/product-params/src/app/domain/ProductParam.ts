import { ProductParamType } from './ProductParamType'

export class ProductParam {
    constructor(
        public id: Number = null,
        public productId: Number = null,
        public value: String = null,
        public bigValue : String = null,
        public type : ProductParamType = null
    ) {
    }
}