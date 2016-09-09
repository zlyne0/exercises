'use strict';

var app = angular.module('productParametersApp', []);

app.factory('ProductRepository', function ($resource) {
	return $resource( 'rest/product/:productId/addParam', { productId: '@productId' } );
}).controller('productParametersController', function($scope, $http) {
	self = this;
	
	self.setSelectedProduct = function(product) {
		$scope.selectedParam = null;
		$scope.selectedProduct = product;
		self.loadProductParameters();
	}
	
	self.setSelectedParam = function(param) {
		$scope.selectedParam = param;
	}
	
	self.loadProductParameters = function() {
		$http.get("rest/product/" + $scope.selectedProduct.id + "/parameters").then(function(response) {
			$scope.productParameters = response.data;
		});
	}
	
	self.loadProductsList = function() {
		$scope.selectedProduct = null;
		$scope.productParameters = [];
		
		$http.get("rest/product/list").then(function(response) {
			$scope.products = response.data;
		});
	}
	
	self.loadProductParametersType = function() {
		$http.get("rest/parameterType/list").then(function(response) {
			$scope.parameterTypes = response.data;
		});
	}
	
	$scope.paramWindowControl = {};
	
	self.onSaveParam = function(param) {
		console.log('onSaveParam ' + JSON.stringify(param));

		var data = {
			id: param.id,
			productId: $scope.selectedProduct.id,
			type: {
				id: param.type.id
			},
			value: param.value, 
			bigValue: param.bigValue 
		};
		$http.post("rest/product/" + $scope.selectedProduct.id + "/addParam", data).then(
			function onSuccess(response) {
				self.loadProductParameters();					
			},
			function onError(response) {
				alert('error status: ' + response.status + ', data: ' + response.data);
			}
		);
		
		$scope.paramWindowControl.hideWindow();
	}
	
	self.openParamWindow = function(modalId) {
		$scope.paramWindowControl.showWindow({});
	}
	
	self.openEditParamWindow = function(modalId) {
		$scope.paramWindowControl.showWindow(
			JSON.parse(JSON.stringify($scope.selectedParam))
		);
	}
	
	self.loadProductParametersType();
	self.loadProductsList();
});

app.directive('productParamEditorWidget', [function() {
	return {
		templateUrl: 'js/productParamEditorWindow.html',
		restrict: 'E',
		scope: {
			parameterTypesData: '=',
			control: '=',
			whenSaveParam: '&'
		},
		link: function($scope, $element, $attrs) {
			$scope.productParamData = {};
			
			$scope.labels = {
				parameterValue: 'Wartość:',
				parameterBigValue: 'Duża wartość:'
			};

			$scope.internalParamWindowControl = $scope.control || {};
			$scope.internalParamWindowControl.showWindow = function(param) {
				$scope.productParamData = param;
				$('#addParamModal').modal("show");				
			}; 
			$scope.internalParamWindowControl.hideWindow = function() {
				$('#addParamModal').modal("hide");
			};
			
			$scope.saveParam = function() {
				$scope.whenSaveParam({
					param: $scope.productParamData
				});
			};
		}
	}
}]);
