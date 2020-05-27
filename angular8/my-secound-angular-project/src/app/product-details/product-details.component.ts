import { Component, OnInit } from '@angular/core';
import { Input } from '@angular/core';
import { Product } from '../product-list/product';
import { ActivatedRoute, Router } from '@angular/router';
import { Output, EventEmitter } from '@angular/core';
import { products } from '../product-list/products';
import { CartService } from '../cart.service';

@Component({
  selector: 'app-product-details',
  templateUrl: './product-details.component.html',
  styleUrls: ['./product-details.component.css']
})
export class ProductDetailsComponent implements OnInit {

  product : Product;

  constructor(private route: ActivatedRoute, private router: Router, private cartService : CartService) { 
    console.log('productDetailsComponent constructor')
  }

  ngOnInit() {
    this.route.paramMap.subscribe(params => {
      console.log(params)
      if (products.length <= +params.get('productId')) {
        this.router.navigate(['products/notfound'])
      } else {
        this.product = products[+params.get('productId')];
      }
    })
  }

  back() {
    this.router.navigate(['/'])
  }

  addToCart(product) {
    this.cartService.addToCart(product)
    window.alert('Your product has been added to the cart');
  }

}