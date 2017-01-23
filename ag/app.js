var app = angular.module("anterApp", ["ngRoute"]);
app.config(function($routeProvider) {
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
    });
});	
