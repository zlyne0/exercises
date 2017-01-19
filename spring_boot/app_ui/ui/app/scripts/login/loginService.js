'use strict';

angular.module('productParametersApp').factory('loginService', function($q, $http, $location) {
    var self = this;
    var loc = $location.protocol() + '://' + $location.host() + ':' + $location.port();
    
    var authenticated = false;
    
    function authenticate(credentials) {
        return $q(function (resolve, reject) {
            var headers = credentials ? {
                Authorization : 'Basic ' + btoa(credentials.username + ':' + credentials.password)
            } : {};
            $http.get(loc + '/session/user/login', {
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
   
    function logout() {
        $http.post(loc + '/logout', {}).finally(function() {
            self.authenticated = false;
        });
    }
    
    return {
        logout : logout,
        authenticate : authenticate,
        isAuthenticated : authenticated
    };
});