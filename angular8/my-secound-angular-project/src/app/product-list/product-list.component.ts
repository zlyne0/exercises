import { Component, OnInit } from '@angular/core';
import { Input } from '@angular/core';
import { Product } from './product';
import { ActivatedRoute } from '@angular/router';
import { Output, EventEmitter } from '@angular/core';
import { products } from './products';

@Component({
  selector: 'app-product-list',
  templateUrl: './product-list.component.html',
  styleUrls: [ './product-list.component.css' ]
})
export class ProductListComponent implements OnInit {

  products = products;
  product : Product;

  constructor(private route: ActivatedRoute) { 
  }

  ngOnInit() {
  }

  onNotify() {

  }

  share() {
    
  }

}