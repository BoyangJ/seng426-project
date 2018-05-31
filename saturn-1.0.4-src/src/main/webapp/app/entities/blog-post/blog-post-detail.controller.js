(function () {
	'use strict';
	angular
		.module('saturnApp')
		.controller('BlogPostDetailController', BlogPostDetailController);

	BlogPostDetailController.$inject = ['$scope', '$rootScope', '$stateParams', '$sce', 'previousState', 'entity', 'BlogPost', 'User', 'Comment', 'AlertService'];

	function BlogPostDetailController($scope, $rootScope, $stateParams, $sce, previousState, entity, BlogPost, User, Comment, AlertService) {
		var vm = this;

		vm.blogPost = entity;
		vm.htmlContent = $sce.trustAsHtml(vm.blogPost.content);
		vm.previousState = previousState.name;
		vm.addComment = addComment;

		vm.comments = {};

		loadAllComments();

		function loadAllComments() {
			Comment.query({
				postId: vm.blogPost.id
			}, function (data) {
				data.forEach(function(comment){
					comment.htmlContent = $sce.trustAsHtml(comment.content);
				});
				vm.comments = data;
			}, function (error) {
				AlertService.error(error.data.message);
			});
		}
		
		function sanitize(unsanitized, sanitized) {
			sanitized = sanitized || "";
			var sp = /(.*?)\[(b|i|url)\](.*?)\[\/\2\]((.|\n)*)/.exec(unsanitized);
			
			if (sp) {
				sanitized += sp[1].replace("<", "&lg;").replace(">", "&gt;");
				
				if (sp[2] === "url") {
					sanitized += "<a href='"+sp[3]+"'>"+sp[3]+"</a>";
				} else {
					sanitized += "<"+sp[2]+">"+sp[3]+"</"+sp[2]+">";
				}
				
				sanitized = sanitize(sp[4], sanitized);
			} else {
				sanitized += unsanitized.replace(/</g, "&lg;").replace(/>/g, "&gt;");
			}

			return sanitized;
		}
		
		sanitize("[b]Hello[/b] and [i]Bye<script>alert(6)</script>[/i]. Look at [url]google.com[/url]<script>alert(5)</script> no [s]sheeeeeet[/s]");

		var unsubscribe = $rootScope.$on('saturnApp:blogPostUpdate', function (event, result) {
			vm.blogPost = result;
		});

		$scope.$on('$destroy', unsubscribe);

		function addComment() {
			vm.isSaving = true;
			vm.comment.post = vm.blogPost;
			Comment.save(vm.comment, function (result) {
				$scope.$emit('saturnApp:commentUpdate', result);
				vm.isSaving = false;

				vm.comment = {};

				loadAllComments();

				$scope.commentForm.$setPristine();
				$scope.commentForm.$setUntouched();
			}, function (error) {
				AlertService.error(error.data.message);
				vm.isSaving = false;
			});
		}
	}
})();
