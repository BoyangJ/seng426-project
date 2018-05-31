(function () {
	'use strict';

	angular
		.module('saturnApp')
		.controller('CommentDeleteController', CommentDeleteController);

	CommentDeleteController.$inject = ['$uibModalInstance', '$stateParams', 'Comment'];

	function CommentDeleteController($uibModalInstance, $stateParams, Comment) {
		var vm = this;

		vm.id = $stateParams.cid;
		vm.clear = clear;
		vm.confirmDelete = confirmDelete;

		function clear() {
			$uibModalInstance.dismiss('cancel');
		}

		function confirmDelete() {
			Comment.delete({id: vm.id},
			function () {
				$uibModalInstance.close(true);
			});
		}
	}
})();
