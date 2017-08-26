function arrayIncludes(array, k) {
	for (var i = 0; i < array.length; i++) {
		if (array[i] === k) {
			return true;
		}
	}
	return false;
}

angular.module('jobOffers', [ 'ngRoute', 'ngSanitize' ])

.component('jobOffers', {
	templateUrl : 'job-offers/jobOffers.html',
	bindings: {
		countries: '='
	},
	controller : function($attrs, jobOffersData) {
		var ctrl = this;
		var selectedCountries = JSON.parse($attrs.countries);
		
		ctrl.jobByCountry = {};
		for (var i=0; i<jobOffersData.length; i++) {
			var country = jobOffersData[i].country;
			if (arrayIncludes(selectedCountries, country)) {
				if (ctrl.jobByCountry[country] == null) {
					ctrl.jobByCountry[country] = [jobOffersData[i]];
				} else {
					ctrl.jobByCountry[country].push(jobOffersData[i]); 
				}
			}
		}
		
		ctrl.actualJobOffer = null;
		if (jobOffersData.length > 0) {
			ctrl.actualJobOffer = jobOffersData[0];
		}
		
		ctrl.showJobOffer = function(jobOffer) {
			ctrl.actualJobOffer = jobOffer;
		}
	}
})
.filter('nl2br', function() {
	return function(input) {
		return (input || '')
			.replace(/\n/g, '<br />')
			.replace(/\r\n/g, '<br />');
	}
})
.component('jobOfferToJson', {
	templateUrl : 'job-offers/jobOfferToJson.html',
	controller : function() {
		var ctrl = this;
		ctrl.jobOffer = {};
	}
})
.config(function($routeProvider) {
	$routeProvider.when('/jobOfferToJson', {
		template : '<job-offer-to-json></job-offer-to-json>'
	});	
});