'use strict';

angular.module('productParametersApp').factory('paramService', function($http, $resource) {
	
	function loadProductParametersType() {
		return $resource('rest/parameterType/list').query().$promise;
	}

	function loadProductList() {
		return $resource('rest/product/list').query().$promise;
	}

	function loadProductParameters(productId) {
		return $resource('rest/product/:productId/parameters')
			.query({productId : productId})
			.$promise;
	}
	
	function createParam(productId, param) {
		return $resource("rest/product/:productId/params", {productId : productId}).save(param).$promise;
	}
	
	return {
		loadProductParametersType : loadProductParametersType,
		loadProductList : loadProductList,
		loadProductParameters : loadProductParameters,
		createParam : createParam
	};
});

