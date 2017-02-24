(function() {
    'use strict';
    angular
        .module('voessApp')
        .factory('UserExt', UserExt);

    UserExt.$inject = ['$resource'];

    function UserExt ($resource) {
        var resourceUrl =  'api/user-exts/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
