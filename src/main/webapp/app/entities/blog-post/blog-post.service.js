(function() {
    'use strict';
    angular
        .module('saturnApp')
        .factory('BlogPost', BlogPost);

    BlogPost.$inject = ['$resource', 'DateUtils'];

    function BlogPost ($resource, DateUtils) {
        var resourceUrl =  'api/blog-posts/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.createdDate = DateUtils.convertDateTimeFromServer(data.createdDate);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
