'use strict';

angular.module('saturnApp')
	.directive('fileUpload', function () {
		return {
			restrict: 'A',
			link: function (scope, el, attrs) {
				el.bind('change', function (event) {
					var files = event.target.files;

					if (attrs.ngModel) {
						scope.$apply(function () {
							if (attrs.multiple) {
								scope[attrs.ngModel] = files;
							} else {
								scope[attrs.ngModel] = files[0];
							}
						});
					}

					//iterate files since 'multiple' may be specified on the element
					for (var i = 0; i < files.length; i++) {
						//emit event upward
						scope.$emit("fileSelected", {file: files[i]});
						//$rootScope.$broadcast
					}
				});
			}
		};
	});