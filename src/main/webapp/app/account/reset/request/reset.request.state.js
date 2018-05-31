(function () {
	'use strict';

	angular
		.module('saturnApp')
		.config(stateConfig);

	stateConfig.$inject = ['$stateProvider'];

	function stateConfig($stateProvider) {
		$stateProvider.state('requestReset', {
			parent: 'account',
			url: '/reset/request',
			data: {
				roles: []
			},
			views: {
				'content@': {
					templateUrl: 'app/account/reset/request/reset.request.html',
					controller: 'RequestResetController',
					controllerAs: 'vm'
				}
			}
		});
	}
})();
