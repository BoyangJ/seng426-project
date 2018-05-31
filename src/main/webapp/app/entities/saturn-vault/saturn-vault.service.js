(function () {
	'use strict';
	angular
		.module('saturnApp')
		.factory('SaturnVault', SaturnVault);

	SaturnVault.$inject = ['$resource', 'DateUtils'];

	function SaturnVault($resource, DateUtils) {
		var resourceUrl = 'api/saturn-vaults/:id';

		return $resource(resourceUrl, {}, {
			'query': {method: 'GET', isArray: true},
			'get': {
				method: 'GET',
				transformResponse: function (data) {
					if (data) {
						data = angular.fromJson(data);
						data.createdDate = DateUtils.convertLocalDateFromServer(data.createdDate);
						data.lastModifiedDate = DateUtils.convertLocalDateFromServer(data.lastModifiedDate);
					}
					return data;
				}
			},
			'update': {
				method: 'PUT',
				transformRequest: function (data) {
					var copy = angular.copy(data);
					copy.createdDate = DateUtils.convertLocalDateToServer(copy.createdDate);
					copy.lastModifiedDate = DateUtils.convertLocalDateToServer(copy.lastModifiedDate);
					return angular.toJson(copy);
				}
			},
			'save': {
				method: 'POST',
				transformRequest: function (data) {
					var copy = angular.copy(data);
					copy.createdDate = DateUtils.convertLocalDateToServer(copy.createdDate);
					copy.lastModifiedDate = DateUtils.convertLocalDateToServer(copy.lastModifiedDate);
					return angular.toJson(copy);
				}
			}
		});
	}
})();
