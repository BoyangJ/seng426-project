(function () {
	'use strict';

	angular
		.module('saturnApp')
		.config(stateConfig);

	stateConfig.$inject = ['$stateProvider'];

	function stateConfig($stateProvider) {
		$stateProvider.state('audits', {
			parent: 'admin',
			url: '/audits',
			data: {
				roles: ['ADMIN'],
				pageTitle: 'Audits'
			},
			views: {
				'content@': {
					templateUrl: 'app/admin/audits/audits.html',
					controller: 'AuditsController',
					controllerAs: 'vm'
				}
			}
		});
	}
})();
