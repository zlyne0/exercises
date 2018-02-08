import { ProductParamType } from '../domain/ProductParamType'
import { Product } from '../domain/Product';
import { ProductParam } from '../domain/ProductParam'
import { ProductListService } from '../domain/ProductListService';
import { Observable, Subscription, Subject } from 'rxjs';
 
import { Component, ViewChild, TemplateRef, OnInit, Output, EventEmitter, Input, OnDestroy } from '@angular/core';
import { ModalDirective } from 'ngx-bootstrap/modal';
import { BsModalService } from 'ngx-bootstrap/modal';
import { BsModalRef } from 'ngx-bootstrap/modal/bs-modal-ref.service';

@Component({
    selector: 'param-details-modal',
    templateUrl: './param-details-modal.component.html'
})
export class ParamDetailsModalComponent implements OnInit, OnDestroy {
    @ViewChild(ModalDirective) modal: ModalDirective;

    @Input() product : Product
    @Input() openModalListener : Observable<ProductParam>
    @Output() confirmParamListener : Subject<ProductParam> = new Subject<ProductParam>()

    param : ProductParam
    
    constructor( private modalService: BsModalService ) {
        this.param = new ProductParam(null, null, null, null, null)
    }
    
    ngOnInit() {
        const self = this;

        this.openModalListener.subscribe(productParam => {
            self.param = productParam
            self.modal.show();
        });
    }

    ngOnDestroy() {
    }

    addParamCloseModal() {
        this.modal.hide()
    }

    confirmAddParam() {
        this.confirmParamListener.next(this.param)
        this.modal.hide()
    }
    
}