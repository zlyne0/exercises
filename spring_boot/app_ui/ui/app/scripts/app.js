'use strict';

/**
 * @ngdoc overview
 * @name appApp
 * @description
 * # appApp
 *
 * Main module of the application.
 */
var app = angular
  .module('productParametersApp', [
    'ngAnimate',
    'ngCookies',
    'ngResource',
    'ngRoute',
    'ngSanitize',
    'ngTouch',
    'ui.router',
    'ui.bootstrap'
  ])
.config(function ($stateProvider, $urlRouterProvider, $httpProvider) {
    $httpProvider.defaults.headers.common['X-Requested-With'] = 'XMLHttpRequest';
    
    var login = {
        name : 'login',
        url : '/login',
        templateUrl : 'scripts/login/loginForm.html',
        controller : 'loginFormCtrl'
    };
    var productParameters = {
        name : 'productParameters',
        url : '/productParameters',
        templateUrl : 'scripts/products.html',
        controller : 'productParametersController'
    };
    $stateProvider.state(login);
    $stateProvider.state(productParameters);
});

app.controller('productParametersController', function($scope, productParamEditorWindowProvider, paramService) {
	var self = this;

	$scope.selectedProduct = {};
	
	self.setSelectedProduct = function(product) {
		$scope.selectedParam = null;
		$scope.selectedProduct = product;
		self.loadProductParameters();
	};

	self.setSelectedParam = function(param) {
		$scope.selectedParam = param;
	};

	self.loadProductParameters = function() {
		paramService.loadProductParameters($scope.selectedProduct.id).then(function(response) {
			$scope.productParameters = response;
		});
	};

	self.loadProductsList = function() {
		$scope.selectedProduct = null;
		$scope.productParameters = [];

		paramService.loadProductList().then(function(response) {
			$scope.products = response;
		});
	};

	self.openParamWindow = function() {
		productParamEditorWindowProvider.openWindow($scope.selectedProduct, null, self.loadProductParameters);
	};

	self.openEditParamWindow = function() {
		productParamEditorWindowProvider.openWindow($scope.selectedProduct, angular.copy($scope.selectedParam), self.loadProductParameters);
	};

	self.loadProductsList();
});
