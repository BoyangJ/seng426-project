(function () {
	'use strict';

	angular
		.module('saturnApp')
		.directive('hasAnyRole', hasAnyRole);

	hasAnyRole.$inject = ['Principal'];

	function hasAnyRole(Principal) {
		var directive = {
			restrict: 'A',
			link: linkFunc
		};

		return directive;

		function linkFunc(scope, element, attrs) {
			var roles = attrs.hasAnyRole.replace(/\s+/g, '').split(',');

			var setVisible = function () {
				element.removeClass('hidden');
			},
				setHidden = function () {
					element.addClass('hidden');
				},
				defineVisibility = function (reset) {
					var result;
					if (reset) {
						setVisible();
					}

					result = Principal.hasAnyRole(roles);
					if (result) {
						setVisible();
					} else {
						setHidden();
					}
				};

			if (roles.length > 0) {
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
