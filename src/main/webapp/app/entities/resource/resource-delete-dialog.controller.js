(function () {
	'use strict';

	angular
		.module('saturnApp')
		.controller('ResourceDeleteController', ResourceDeleteController);

	ResourceDeleteController.$inject = ['$uibModalInstance', '$stateParams', 'Resource'];

	function ResourceDeleteController($uibModalInstance, $stateParams, Resource) {
		var vm = this;

		vm.fileName = $stateParams.filename;

		vm.clear = clear;
		vm.confirmDelete = confirmDelete;

		function clear() {
			$uibModalInstance.dismiss('cancel');
		}

		function confirmDelete() {
			Resource.delete({fileName: vm.fileName},
			function () {
				$uibModalInstance.close(true);
			});
		}
	}
})();
