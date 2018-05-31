(function () {
	'use strict';

	angular
		.module('saturnApp')
		.config(stateConfig);

	stateConfig.$inject = ['$stateProvider'];

	function stateConfig($stateProvider) {
		$stateProvider.state('technology', {
			parent: 'app',
			url: '/technology',
			data: {
				roles: []
			},
			views: {
				'content@': {
					templateUrl: 'app/technology/technology.html',
					controller: 'TechnologyController',
					controllerAs: 'vm'
				}
			}
		});
	}
})();
