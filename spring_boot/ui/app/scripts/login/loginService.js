'use strict';

angular.module('productParametersApp').factory('loginService', function($q, $http) {
    
    var self = this;
    
    var authenticated = false;
    
    function authenticate(credentials) {
        return $q(function (resolve, reject) {
            var headers = credentials ? {
                Authorization : 'Basic ' + btoa(credentials.username + ':' + credentials.password)
            } : {};
            
            $http.get('rest/user/login', {
                headers : headers
            }).then(function(response) {
                if (response.data.name) {
                    self.authenticated = true;
                } else {
                    self.authenticated = false;
                }
                resolve(self.authenticated);
            }, function(error) {
                self.authenticated = false;
                reject(error);
            });
        });
    }
    
    return {
        authenticate : authenticate,
        isAuthenticated : authenticated
    };
});