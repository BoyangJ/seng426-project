(function () {
	'use strict';

	angular
		.module('saturnApp')
		.directive('hasRole', hasRole);

	hasRole.$inject = ['Principal'];

	function hasRole(Principal) {
		var directive = {
			restrict: 'A',
			link: linkFunc
		};

		return directive;

		function linkFunc(scope, element, attrs) {
			var role = attrs.hasRole.replace(/\s+/g, '');

			var setVisible = function () {
				element.removeClass('hidden');
			};

			var setHidden = function () {
				element.addClass('hidden');
			};

			var defineVisibility = function (reset) {
				if (reset) {
					setVisible();
				}

				Principal.hasRole(role).then(function (result) {
					if (result) {
						setVisible();
					} else {
						setHidden();
					}
				});
			};

			if (role.length > 0) {
				defineVisibility(true);

				scope.$watch(function () {
					return Principal.isAuthenticated();
				}, function () {
					defineVisibility(true);
				});
			}
		}
	}
})();
