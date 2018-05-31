(function () {
	'use strict';

	angular
		.module('saturnApp')
		.config(stateConfig);

	stateConfig.$inject = ['$stateProvider'];

	function stateConfig($stateProvider) {
		$stateProvider
			.state('resource', {
				parent: 'entity',
				url: '/resource',
				data: {
					authorities: ["EMPLOYEE", "MANAGER", "ADMIN"],
					pageTitle: 'Resources'
				},
				views: {
					'content@': {
						templateUrl: 'app/entities/resource/resources.html',
						controller: 'ResourceController',
						controllerAs: 'vm'
					}
				},
				resolve: {
					pagingParams: [function () {
							return {};
						}]
				}
			})
			.state('resource.new', {
				parent: 'resource',
				url: '/new',
				data: {
					authorities: ["EMPLOYEE", "MANAGER", "ADMIN"]
				},
				onEnter: ['$state', '$uibModal', function ($state, $uibModal) {
						$uibModal.open({
							templateUrl: 'app/entities/resource/resource-dialog.html',
							controller: 'ResourceDialogController',
							controllerAs: 'vm',
							backdrop: 'static',
							size: 'md',
							resolve: {}
						}).result.then(function () {
							$state.go('resource', null, {reload: 'resource'});
						}, function () {
							$state.go('resource');
						});
					}]
			})
			.state('resource.delete', {
				parent: 'resource',
				url: '/{filename}/delete',
				data: {
					authorities: ["EMPLOYEE", "MANAGER", "ADMIN"]
				},
				onEnter: ['$stateParams', '$state', '$uibModal', function ($stateParams, $state, $uibModal) {
						$uibModal.open({
							templateUrl: 'app/entities/resource/resource-delete-dialog.html',
							controller: 'ResourceDeleteController',
							controllerAs: 'vm',
							size: 'md',
							resolve: {}
						}).result.then(function () {
							$state.go('resource', null, {reload: 'resource'});
						}, function () {
							$state.go('^');
						});
					}]
			});
	}
})();
