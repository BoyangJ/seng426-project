(function () {
	'use strict';

	angular
		.module('saturnApp')
		.config(stateConfig);

	stateConfig.$inject = ['$stateProvider'];

	function stateConfig($stateProvider) {
		$stateProvider
			.state('blog-post-main', {
				parent: 'app',
				url: '/blog',
				data: {
					roles: []
				},
				views: {
					'content@': {
						templateUrl: 'app/entities/blog-post/blog-post-main.html',
						controller: 'BlogPostMainController',
						controllerAs: 'vm'
					}
				}
			})
			.state('blog-post', {
				parent: 'entity',
				url: '/blog-post?page&sort&search',
				data: {
					authorities: [],
					pageTitle: 'BlogPosts'
				},
				views: {
					'content@': {
						templateUrl: 'app/entities/blog-post/blog-posts.html',
						controller: 'BlogPostController',
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
			.state('blog-post-detail', {
				parent: 'entity',
				url: '/blog-post/{id}',
				data: {
					authorities: [],
					pageTitle: 'BlogPost'
				},
				views: {
					'content@': {
						templateUrl: 'app/entities/blog-post/blog-post-detail.html',
						controller: 'BlogPostDetailController',
						controllerAs: 'vm'
					}
				},
				resolve: {
					entity: ['$stateParams', 'BlogPost', function ($stateParams, BlogPost) {
							return BlogPost.get({id: $stateParams.id}).$promise;
						}],
					previousState: ["$state", function ($state) {
							var currentStateData = {
								name: $state.current.name || 'blog-post',
								params: $state.params,
								url: $state.href($state.current.name, $state.params)
							};
							return currentStateData;
						}]
				}
			})
			.state('blog-post-detail.edit', {
				parent: 'blog-post-detail',
				url: '/detail/edit',
				data: {
					authorities: ["EMPLOYEE", "MANAGER", "ADMIN"]
				},
				onEnter: ['$stateParams', '$state', '$uibModal', function ($stateParams, $state, $uibModal) {
						$uibModal.open({
							templateUrl: 'app/entities/blog-post/blog-post-dialog.html',
							controller: 'BlogPostDialogController',
							controllerAs: 'vm',
							backdrop: 'static',
							size: 'lg',
							resolve: {
								entity: ['BlogPost', function (BlogPost) {
										return BlogPost.get({id: $stateParams.id}).$promise;
									}]
							}
						}).result.then(function () {
							$state.go('^', {}, {reload: false});
						}, function () {
							$state.go('^');
						});
					}]
			})
			.state('blog-post.new', {
				parent: 'blog-post',
				url: '/new',
				data: {
					authorities: ["EMPLOYEE", "MANAGER", "ADMIN"]
				},
				onEnter: ['$stateParams', '$state', '$uibModal', function ($stateParams, $state, $uibModal) {
						$uibModal.open({
							templateUrl: 'app/entities/blog-post/blog-post-dialog.html',
							controller: 'BlogPostDialogController',
							controllerAs: 'vm',
							backdrop: 'static',
							size: 'lg',
							resolve: {
								entity: function () {
									return {
										title: null,
										content: null,
										date: null,
										type: null,
										id: null
									};
								}
							}
						}).result.then(function () {
							$state.go('blog-post', null, {reload: 'blog-post'});
						}, function () {
							$state.go('blog-post');
						});
					}]
			})
			.state('blog-post.edit', {
				parent: 'blog-post',
				url: '/{id}/edit',
				data: {
					authorities: ["EMPLOYEE", "MANAGER", "ADMIN"]
				},
				onEnter: ['$stateParams', '$state', '$uibModal', function ($stateParams, $state, $uibModal) {
						$uibModal.open({
							templateUrl: 'app/entities/blog-post/blog-post-dialog.html',
							controller: 'BlogPostDialogController',
							controllerAs: 'vm',
							backdrop: 'static',
							size: 'lg',
							resolve: {
								entity: ['BlogPost', function (BlogPost) {
										return BlogPost.get({id: $stateParams.id}).$promise;
									}]
							}
						}).result.then(function () {
							$state.go('blog-post', null, {reload: 'blog-post'});
						}, function () {
							$state.go('^');
						});
					}]
			})
			.state('blog-post.delete', {
				parent: 'blog-post',
				url: '/{id}/delete',
				data: {
					authorities: ["EMPLOYEE", "MANAGER", "ADMIN"]
				},
				onEnter: ['$stateParams', '$state', '$uibModal', function ($stateParams, $state, $uibModal) {
						$uibModal.open({
							templateUrl: 'app/entities/blog-post/blog-post-delete-dialog.html',
							controller: 'BlogPostDeleteController',
							controllerAs: 'vm',
							size: 'md',
							resolve: {
								entity: ['BlogPost', function (BlogPost) {
										return BlogPost.get({id: $stateParams.id}).$promise;
									}]
							}
						}).result.then(function () {
							$state.go('blog-post', null, {reload: 'blog-post'});
						}, function () {
							$state.go('^');
						});
					}]
			});
	}

})();
