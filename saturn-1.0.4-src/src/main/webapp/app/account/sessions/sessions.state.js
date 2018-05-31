(function () {
	'use strict';

	angular
		.module('saturnApp')
		.config(stateConfig);

	stateConfig.$inject = ['$stateProvider'];

	function stateConfig($stateProvider) {
		$stateProvider.state('sessions', {
			parent: 'account',
			url: '/sessions',
			data: {
				roles: ['USER', 'EMPLOYEE', 'MANAGER', 'ADMIN'],
				pageTitle: 'Sessions'
			},
			views: {
				'content@': {
					templateUrl: 'app/account/sessions/sessions.html',
					controller: 'SessionsController',
					controllerAs: 'vm'
				}
			}
		});
	}
})();
