(function () {
	'use strict';

	angular
		.module('saturnApp')
		.config(stateConfig);

	stateConfig.$inject = ['$stateProvider'];

	function stateConfig($stateProvider) {
		$stateProvider.state('settings', {
			parent: 'account',
			url: '/settings',
			data: {
				roles: ['USER', 'EMPLOYEE', 'MANAGER', 'ADMIN'],
				pageTitle: 'Settings'
			},
			views: {
				'content@': {
					templateUrl: 'app/account/settings/settings.html',
					controller: 'SettingsController',
					controllerAs: 'vm'
				}
			}
		});
	}
})();
