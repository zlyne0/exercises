(function() {
'use strict';

angular
.module("anterApp", ["ngRoute", "ngMaterial"])
.config(function($routeProvider, $mdThemingProvider) {
	
    $routeProvider
    .when("/", {
        templateUrl : "aboutUs.html"
    })
    .when("/employer", {
        templateUrl : "employer.html"
    })
    .when("/forEmployer", {
    	templateUrl : "forEmployer.html"
    })
    .when("/employee", {
        templateUrl : "employee.html"
    })
    .when("/aboutUs", {
        templateUrl : "aboutUs.html"
    })
    .when("/contact", {
        templateUrl : "contact.html"
    })
    .when("/workInPolandForForeigners", {
    	templateUrl : "workInPolandForForeigners.html"
    })
    .when("/foreignersService", {
    	templateUrl : "foreignersService.html"
    })
    
    .when("/workNetherlands", {
    	templateUrl : "workNetherlands.html"
    })
    .when("/workFrance", {
    	templateUrl : "workFrance.html"
    })
    .when("/workGermany", {
    	templateUrl : "workGermany.html"
    })
    .when("/workBelgium", {
    	templateUrl : "workBelgium.html"
    })
    
})	
.controller('MenuCtrl', function MenuCtrl($mdDialog, $location) {
	var originatorEv;
	this.openMenu = function($mdMenu, ev) {
		originatorEv = ev;
		$mdMenu.open(ev);
	};
	
	this.menuButtonClick = function(routeStr) {
		$location.path(routeStr);
	};
});

})();