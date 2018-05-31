(function () {
	'use strict';

	angular
		.module('saturnApp')
		.controller('SaturnVaultDeleteController', SaturnVaultDeleteController);

	SaturnVaultDeleteController.$inject = ['$uibModalInstance', 'entity', 'SaturnVault'];

	function SaturnVaultDeleteController($uibModalInstance, entity, SaturnVault) {
		var vm = this;

		vm.saturnPass = entity;
		vm.clear = clear;
		vm.confirmDelete = confirmDelete;

		function clear() {
			$uibModalInstance.dismiss('cancel');
		}

		function confirmDelete(id) {
			SaturnVault.delete({id: id},
			function () {
				$uibModalInstance.close(true);
			});
		}
	}
})();
