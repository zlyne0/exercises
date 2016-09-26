'use strict';

angular.module('productParametersApp').controller('paramEditionController', function($scope, $uibModalInstance, paramService, windowInputData) {
	$scope.labels = {
		parameterValue: 'Wartość:',
		parameterBigValue: 'Duża wartość:'
	};
	
	$scope.parameterTypesData = [];
	$scope.productParamData = {
		id : null
	};
	$scope.product = windowInputData.product;
	
	if (windowInputData.selectedParam !== null) {
		$scope.productParamData = windowInputData.selectedParam;
	}
	
	paramService.loadProductParametersType().then(function(response) {
		$scope.parameterTypesData = response;
	});
	
	$scope.close = function() {
		$uibModalInstance.close();
	}; 

	$scope.saveParam = function() {
		var param = $scope.productParamData;
		
		console.log('parameterTypesData ' + JSON.stringify(param));
		
		var data = {
			id : param.id,
			productId : $scope.product.id,
			type : {
				id : param.type.id
			},
			value : param.value,
			bigValue : param.bigValue
		};
		
		paramService.createParam($scope.product.id, data).then(function() {
			$uibModalInstance.close();
			windowInputData.onParamSaveListener();
		}, function(errorResult) {
			console.log('error ' + errorResult);
		});
		
	};	
	
});

