(function() {
    'use strict';

    angular
        .module('saturnApp')
        .controller('BlogPostDeleteController',BlogPostDeleteController);

    BlogPostDeleteController.$inject = ['$uibModalInstance', 'entity', 'BlogPost'];

    function BlogPostDeleteController($uibModalInstance, entity, BlogPost) {
        var vm = this;

        vm.blogPost = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            BlogPost.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
