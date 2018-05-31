(function () {
	'use strict';
	angular
		.module('saturnApp')
		.factory('Comment', Comment);

	Comment.$inject = ['$resource'];

	function Comment($resource) {
		var resourceUrl = 'api/comments/:id';

		return $resource(resourceUrl, {}, {
			'query': { method: 'GET', isArray: true}
		});
	}
})();
