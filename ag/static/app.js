(function() {'use strict';

angular.module("anterApp", ["ngRoute", "ngMap"])
.config(function($routeProvider) {
    $routeProvider
    .when("/", {
        templateUrl : "company.html"
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