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
            $http.get(loc + '/rest/user/login', {
                headers : headers
            }).then(function(response) {
                if (response.data.name) {
                    getToken();
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
    
    function getToken() {
        $http.get(loc + '/token').then(function(response) {
            var token = response.data.token;
            
            $http({
                url : 'http://localhost:18080/rest/product/list',
                method : 'GET',
                headers : {
                    'x-auth-token' : response.data.token
                }
            }).then(function(response) {
                console.log('product list ' + angular.toJson(response))
            });            
            
        })
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