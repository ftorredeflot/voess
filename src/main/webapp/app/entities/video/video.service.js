(function() {
    'use strict';
    angular
        .module('voessApp')
        .factory('Video', Video);

    Video.$inject = ['$resource', 'DateUtils'];

    function Video ($resource, DateUtils) {
        var resourceUrl =  'api/videos/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.videoDate = DateUtils.convertDateTimeFromServer(data.videoDate);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
