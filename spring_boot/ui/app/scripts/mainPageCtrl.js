'use strict';

angular.module('productParametersApp').controller('mainPageCtrl', function($scope, $state, loginService) {
    loginService.authenticate().then(function(authenticated) {
        if (authenticated) {
            $state.go('productParameters');
        } else {
            $state.go('login');
        }
    }, function() {
        $state.go('login');
    });
});
