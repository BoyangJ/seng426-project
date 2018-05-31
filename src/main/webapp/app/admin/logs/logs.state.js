(function () {
	'use strict';

	angular
		.module('saturnApp')
		.config(stateConfig);

	stateConfig.$inject = ['$stateProvider'];

	function stateConfig($stateProvider) {
		$stateProvider.state('logs', {
			parent: 'admin',
			url: '/logs',
			data: {
				roles: ['ADMIN'],
				pageTitle: 'Logs'
			},
			views: {
				'content@': {
					templateUrl: 'app/admin/logs/logs.html',
					controller: 'LogsController',
					controllerAs: 'vm'
				}
			}
		});
	}
})();
