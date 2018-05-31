(function () {
	'use strict';

	angular
		.module('saturnApp')
		.config(stateConfig);

	stateConfig.$inject = ['$stateProvider'];

	function stateConfig($stateProvider) {
		$stateProvider
			.state('error', {
				parent: 'app',
				url: '/error',
				data: {
					roles: [],
					pageTitle: 'Error page!'
				},
				views: {
					'content@': {
						templateUrl: 'app/layouts/error/error.html'
					}
				}
			})
			.state('accessdenied', {
				parent: 'app',
				url: '/accessdenied',
				data: {
					roles: []
				},
				views: {
					'content@': {
						templateUrl: 'app/layouts/error/accessdenied.html'
					}
				}
			});
	}
})();
