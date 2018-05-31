(function () {
	'use strict';

	angular
		.module('saturnApp')
		.factory('Register', Register);

	Register.$inject = ['$resource'];

	function Register($resource) {
		return $resource('api/register', {}, {});
	}
})();
