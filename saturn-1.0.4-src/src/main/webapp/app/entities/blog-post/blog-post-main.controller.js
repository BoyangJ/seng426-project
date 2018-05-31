(function () {
	'use strict';

	angular
		.module('saturnApp')
		.controller('BlogPostMainController', MediaController);

	MediaController.$inject = ['$scope', '$state', 'BlogPost', 'AlertService'];

	function MediaController($scope, $state, BlogPost, AlertService) {
		var vm = this;
		
		vm.posts = {
			NEWS: [],
			BLOG: []
		};

		loadAll();

		function loadAll() {
			BlogPost.query({}, function (data) {
				data.forEach(function(post){
					vm.posts[post.type].push(post);
				});
			}, function (error) {
				AlertService.error(error.data.message);
			});
		}
	}
})();
