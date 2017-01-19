'use strict';

angular.module('productParametersApp').factory('paramService', function($http, $resource, $location) {
    var loc = $location.protocol() + '://' + $location.host() + ':' + $location.port();
    
	function loadProductParametersType() {
		return $resource(loc + '/rest/parameterType/list').query().$promise;
	}

	function loadProductList() {
		return $resource(loc + '/rest/product/list').query().$promise;
	}

	function loadProductParameters(productId) {
		return $resource(loc + '/rest/product/:productId/parameters')
			.query({productId : productId})
			.$promise;
	}
	
	function createParam(productId, param) {
		return $resource(loc + '/rest/product/:productId/params', {productId : productId}).save(param).$promise;
	}
	
	return {
		loadProductParametersType : loadProductParametersType,
		loadProductList : loadProductList,
		loadProductParameters : loadProductParameters,
		createParam : createParam
	};
});

