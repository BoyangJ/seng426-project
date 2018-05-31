(function () {
	'use strict';

	angular
		.module('saturnApp')
		.config(stateConfig);

	stateConfig.$inject = ['$stateProvider'];

	function stateConfig($stateProvider) {
		$stateProvider
			.state('comment-delete', {
				url: '/comment/{cid}/delete',
				parent: 'blog-post-detail',
				data: {
                authorities: ['ADMIN', 'MANAGER', 'EMPLOYEE']
				},
				onEnter: ['$stateParams', '$state', '$uibModal', function ($stateParams, $state, $uibModal) {
						$uibModal.open({
							templateUrl: 'app/entities/blog-post/comment/comment-delete-dialog.html',
							controller: 'CommentDeleteController',
							controllerAs: 'vm',
							size: 'md'
						}).result.then(function () {
							$state.go('^', null, {reload: true});
						}, function () {
							$state.go('^');
						});
					}]
			});
	}

})();
