(function () {
	'use strict';
	angular
		.module('saturnApp')
		.factory('Resource', Resource);

	Resource.$inject = ['$resource', 'DateUtils'];

	function Resource($resource, DateUtils) {
		var resourceUrl = 'api/mediaFiles/:fileName';

		return $resource(resourceUrl, {}, {
			'query': {method: 'GET', isArray: true},
			'get': {
				method: 'GET',
				responseType: "blob",
				transformResponse: function (data) {
					if (data) {
						return {
							data: new Blob([data], {'type': 'attachment/plk'})
						};
					}
				}
			},
			'update': {method: 'PUT'},
			'save': {
				method: 'POST',
				isArray: true,
				headers: {'Content-Type': undefined}, //Set the Content-Type header to undefined so that browser can take care of form data boundaries
				transformRequest: function (data) {
					if (data == undefined) {
						return data;
					}

					var fd = new FormData();

					fd.append("file", data["file"]);

					return fd;
				}
			}
		});
	}
})();
