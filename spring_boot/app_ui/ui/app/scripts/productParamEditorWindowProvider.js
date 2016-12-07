'use strict';

angular.module('productParametersApp').factory('productParamEditorWindowProvider', function($uibModal) {

	function openWindow(product, selectedParam, onParamSaveListener) {
		$uibModal.open({
			backdrop : 'static',
			keyboard : false,
			templateUrl : 'scripts/productParamEditorWindow.html',
			controller : 'paramEditionController',
			resolve : {
				windowInputData : function() {
					return {
						product : product,
						selectedParam : selectedParam,
						onParamSaveListener : onParamSaveListener
					};
				}
			}
		});
	}
	
	return {
		openWindow : openWindow
	};
	
});

