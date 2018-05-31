(function () {
	'use strict';

	angular
		.module('saturnApp')
		.controller('ResourceDialogController', ResourceDialogController);

	ResourceDialogController.$inject = ['$timeout', '$scope', '$uibModalInstance', 'Resource'];

	function ResourceDialogController($timeout, $scope, $uibModalInstance, Resource) {
		var vm = this;

		vm.clear = clear;
		vm.save = save;
		
		$timeout(function () {
			angular.element('.form-group:eq(1)>input').focus();
		});

		function clear() {
			$uibModalInstance.dismiss('cancel');
		}

		function save() {
			vm.loadingFile = true;

			Resource.save({file:$scope.uploadFile}, function (result) {
				$scope.$emit('saturnApp:resourceUpdate', result);
				$uibModalInstance.close(result);
				vm.loadingFile = false;
			}, function () {
				vm.loadingFile = false;
			});
		}
	}
})();
