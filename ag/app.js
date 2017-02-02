(function() {'use strict';

angular.module("anterApp", ["ngRoute"])
.config(function($routeProvider) {
    $routeProvider
    .when("/", {
        templateUrl : "main.html"
    })
    .when("/employer", {
        templateUrl : "employer.html"
    })
    .when("/employee", {
        templateUrl : "employee.html"
    })
    .when("/company", {
        templateUrl : "company.html"
    })
    .when("/contact", {
        templateUrl : "contact.html"
    });
});	

})();