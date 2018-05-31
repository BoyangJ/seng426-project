(function () {
	'use strict';

	angular
		.module('saturnApp')
		.controller('BlogPostDialogController', BlogPostDialogController);

	BlogPostDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'BlogPost', 'Principal'];

	function BlogPostDialogController($timeout, $scope, $stateParams, $uibModalInstance, entity, BlogPost, Principal) {
		var vm = this;

		vm.blogPost = entity;
		vm.clear = clear;
		vm.datePickerOpenStatus = {};
		vm.openCalendar = openCalendar;
		vm.save = save;
		vm.user;

		$timeout(function () {
			angular.element('.form-group:eq(1)>input').focus();
		});

		Principal.identity().then(function (account) {
			vm.user = account;
		});

		function clear() {
			$uibModalInstance.dismiss('cancel');
		}

		function save() {
			vm.isSaving = true;
			vm.blogPost.author = vm.user;
			if (vm.blogPost.id !== null) {
				BlogPost.update(vm.blogPost, onSaveSuccess, onSaveError);
			} else {
				BlogPost.save(vm.blogPost, onSaveSuccess, onSaveError);
			}
		}

		function onSaveSuccess(result) {
			$scope.$emit('saturnApp:blogPostUpdate', result);
			$uibModalInstance.close(result);
			vm.isSaving = false;
		}

		function onSaveError() {
			vm.isSaving = false;
		}

		vm.datePickerOpenStatus.date = false;

		function openCalendar(date) {
			vm.datePickerOpenStatus[date] = true;
		}
	}
})();
