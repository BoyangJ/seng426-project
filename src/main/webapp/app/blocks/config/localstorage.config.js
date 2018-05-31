(function () {
	'use strict';

	angular
		.module('saturnApp')
		.config(localStorageConfig);

	localStorageConfig.$inject = ['$localStorageProvider', '$sessionStorageProvider'];

	function localStorageConfig($localStorageProvider, $sessionStorageProvider) {
		$localStorageProvider.setKeyPrefix('jhi-');
		$sessionStorageProvider.setKeyPrefix('jhi-');
	}
})();
