(function () {
	'use strict';

	angular
		.module('saturnApp')
		.config(stateConfig);

	stateConfig.$inject = ['$stateProvider'];

	function stateConfig($stateProvider) {
		$stateProvider.state('jhi-configuration', {
			parent: 'admin',
			url: '/configuration',
			data: {
				roles: ['ADMIN'],
				pageTitle: 'Configuration'
			},
			views: {
				'content@': {
					templateUrl: 'app/admin/configuration/configuration.html',
					controller: 'JhiConfigurationController',
					controllerAs: 'vm'
				}
			}
		});
	}
})();
