(function () {
	'use strict';

	angular
		.module('saturnApp')
		.controller('SaturnVaultDialogController', SaturnVaultDialogController);

	SaturnVaultDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModal', '$uibModalInstance', 'entity', 'SaturnVault', 'User'];

	function SaturnVaultDialogController($timeout, $scope, $stateParams, $uibModal, $uibModalInstance, entity, SaturnVault, User) {
		var vm = this;

		vm.saturnPass = entity;
		vm.datePickerOpenStatus = {};
		vm.openCalendar = openCalendar;
		vm.openPwdGenModal = openPwdGenModal;
		vm.save = save;
		vm.clear = clear;
		vm.users = User.query();
		vm.pwdVisible = false;

		$timeout(function () {
			angular.element('.form-group:eq(1)>input').focus();
		});

		function openPwdGenModal() {
			$uibModal.open({
				templateUrl: 'app/entities/saturn-vault/saturn-vault-pwd-gen.html',
				controller: 'SaturnVaultPwdGenController',
				controllerAs: 'vm',
				backdrop: 'static',
				size: 'sm'
			}).result.then(function (password) {
				vm.saturnPass.password = password;
			}, function () {
			});
		}

		function clear() {
			$uibModalInstance.dismiss('cancel');
		}

		function save() {
			vm.isSaving = true;
			if (vm.saturnPass.id !== null) {
				SaturnVault.update(vm.saturnPass, onSaveSuccess, onSaveError);
			} else {
				SaturnVault.save(vm.saturnPass, onSaveSuccess, onSaveError);
			}
		}

		function onSaveSuccess(result) {
			$scope.$emit('saturnApp:SaturnVaultUpdate', result);
			$uibModalInstance.close(result);
			vm.isSaving = false;
		}

		function onSaveError() {
			vm.isSaving = false;
		}

		vm.datePickerOpenStatus.createdDate = false;
		vm.datePickerOpenStatus.lastModifiedDate = false;

		function openCalendar(date) {
			vm.datePickerOpenStatus[date] = true;
		}
	}
})();
