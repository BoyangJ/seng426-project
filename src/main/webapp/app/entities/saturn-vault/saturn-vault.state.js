(function () {
	'use strict';

	angular
		.module('saturnApp')
		.config(stateConfig);

	stateConfig.$inject = ['$stateProvider'];

	function stateConfig($stateProvider) {
		$stateProvider
			.state('saturn-vault', {
				parent: 'entity',
				url: '/saturn-vault?page&sort&search',
				data: {
					roles: ['USER', 'EMPLOYEE', 'MANAGER', 'ADMIN'],
					pageTitle: 'SaturnVaults'
				},
				views: {
					'content@': {
						templateUrl: 'app/entities/saturn-vault/saturn-vaults.html',
						controller: 'SaturnVaultController',
						controllerAs: 'vm'
					}
				},
				params: {
					page: {
						value: '1',
						squash: true
					},
					sort: {
						value: 'id,asc',
						squash: true
					},
					search: null
				},
				resolve: {
					pagingParams: ['$stateParams', 'PaginationUtil', function ($stateParams, PaginationUtil) {
							return {
								page: PaginationUtil.parsePage($stateParams.page),
								sort: $stateParams.sort,
								predicate: PaginationUtil.parsePredicate($stateParams.sort),
								ascending: PaginationUtil.parseAscending($stateParams.sort),
								search: $stateParams.search
							};
						}]
				}
			})
			.state('saturn-vault.new', {
				parent: 'saturn-vault',
				url: '/new',
				data: {
					roles: ['USER', 'EMPLOYEE', 'MANAGER', 'ADMIN']
				},
				onEnter: ['$stateParams', '$state', '$uibModal', function ($stateParams, $state, $uibModal) {
						$uibModal.open({
							templateUrl: 'app/entities/saturn-vault/saturn-vault-dialog.html',
							controller: 'SaturnVaultDialogController',
							controllerAs: 'vm',
							backdrop: 'static',
							size: 'lg',
							resolve: {
								entity: function () {
									return {
										site: null,
										login: null,
										password: null,
										createdDate: null,
										lastModifiedDate: null,
										id: null
									};
								}
							}
						}).result.then(function () {
							$state.go('saturn-vault', null, {reload: 'saturn-vault'});
						}, function () {
							$state.go('saturn-vault');
						});
					}]
			})
			.state('saturn-vault.edit', {
				parent: 'saturn-vault',
				url: '/{id}/edit',
				data: {
					roles: ['USER', 'EMPLOYEE', 'MANAGER', 'ADMIN']
				},
				onEnter: ['$stateParams', '$state', '$uibModal', function ($stateParams, $state, $uibModal) {
						$uibModal.open({
							templateUrl: 'app/entities/saturn-vault/saturn-vault-dialog.html',
							controller: 'SaturnVaultDialogController',
							controllerAs: 'vm',
							backdrop: 'static',
							size: 'lg',
							resolve: {
								entity: ['SaturnVault', function (SaturnVault) {
										return SaturnVault.get({id: $stateParams.id}).$promise;
									}]
							}
						}).result.then(function () {
							$state.go('saturn-vault', null, {reload: 'saturn-vault'});
						}, function () {
							$state.go('^');
						});
					}]
			})
			.state('saturn-vault.delete', {
				parent: 'saturn-vault',
				url: '/{id}/delete',
				data: {
					roles: ['USER', 'EMPLOYEE', 'MANAGER', 'ADMIN']
				},
				onEnter: ['$stateParams', '$state', '$uibModal', function ($stateParams, $state, $uibModal) {
						$uibModal.open({
							templateUrl: 'app/entities/saturn-vault/saturn-vault-delete-dialog.html',
							controller: 'SaturnVaultDeleteController',
							controllerAs: 'vm',
							size: 'md',
							resolve: {
								entity: ['SaturnVault', function (SaturnVault) {
										return SaturnVault.get({id: $stateParams.id}).$promise;
									}]
							}
						}).result.then(function () {
							$state.go('saturn-vault', null, {reload: 'saturn-vault'});
						}, function () {
							$state.go('^');
						});
					}]
			});
	}

})();
