var app = angular.module('zzz', []);

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
