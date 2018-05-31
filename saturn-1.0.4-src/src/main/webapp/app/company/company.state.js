(function () {
	'use strict';

	angular
		.module('saturnApp')
		.config(stateConfig);

	stateConfig.$inject = ['$stateProvider'];

	function stateConfig($stateProvider) {
		$stateProvider.state('company', {
			parent: 'app',
			url: '/company',
			data: {
				roles: []
			},
			views: {
				'content@': {
					templateUrl: 'app/company/company.html',
					controller: 'CompanyController',
					controllerAs: 'vm'
				}
			}
		}).state('resources', {
			parent: 'company',
			url: '/company/resources',
			data: {
				roles: []
			},
			views: {
				'content@': {
					templateUrl: 'app/company/resources/resources.html',
					controller: 'CompanyController',
					controllerAs: 'vm'
				}
			}
		});
	}
})();
