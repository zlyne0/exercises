'use strict';

angular.module('productParametersApp').controller('loginFormCtrl', function($scope, $state, loginService, $log) {
    $scope.authMsg = null;
    $scope.credentials = {};
    
    $scope.login = function() {
        $scope.authMsg = null;
        
        loginService.authenticate($scope.credentials).then(function(authenticated) {
            if (authenticated) {
                $state.go('productParameters');
            } else {
                $scope.authMsg = 'bledne haslo';
            }
        }, function(error) {
            $log.debug('service error: ' + angular.toJson(error));
            $scope.authMsg = 'blad aplikacji';
        });
    };
});
