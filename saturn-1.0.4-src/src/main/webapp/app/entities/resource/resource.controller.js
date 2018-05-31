(function () {
	'use strict';

	angular
		.module('saturnApp')
		.controller('ResourceController', ResourceController);

	ResourceController.$inject = ['$scope', '$state', 'Resource', 'AlertService'];

	function ResourceController($scope, $state, Resource, AlertService) {
		var vm = this;

		vm.transition = transition;

		loadAll();

		function loadAll() {
			Resource.query({}, function (data, headers) {
				vm.resources = data.map(function (v) {
					v.formattedSize = formatFileSize(v.size);
					return v;
				});
			}, function (error) {
				AlertService.error(error.data.message);
			});
		}

		function transition() {
			$state.transitionTo($state.$current, {});
		}

		function formatFileSize(bytes) {
			var units = ['bytes', 'kB', 'MB', 'GB', 'TB', 'PB'],
				number = Math.floor(Math.log(bytes) / Math.log(1024));
			return (bytes / Math.pow(1024, Math.floor(number))).toFixed(2) + ' ' + units[number];
		}
	}
})();
