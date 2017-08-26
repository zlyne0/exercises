(function() {
'use strict';

angular
.module("anterApp", ["ngRoute", "ngMaterial", 'jobOffers', 'pascalprecht.translate'] )
.config(function($routeProvider, $translateProvider) {
	
	$translateProvider.useStaticFilesLoader({
		prefix: 'locale/locale-',
		suffix: '.json'
	});	
	$translateProvider.preferredLanguage('pl_PL');

	
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

    .when("/workOffers", {
    	templateUrl : "workOffers.html"
    })
    
})	
.controller('MenuCtrl', function MenuCtrl($mdDialog, $location, $translate) {
	var originatorEv;
	this.openMenu = function($mdMenu, ev) {
		originatorEv = ev;
		$mdMenu.open(ev);
	};
	
	this.menuButtonClick = function(routeStr) {
		$location.path(routeStr);
	};
})
.controller('LangCtrl', function($translate) {
	this.changeLang = function(lang) {
		$translate.use(lang);
	};
});

})();