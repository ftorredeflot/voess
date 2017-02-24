(function() {
    'use strict';
    angular
        .module('voessApp')
        .factory('UserLikedVideo', UserLikedVideo);

    UserLikedVideo.$inject = ['$resource', 'DateUtils'];

    function UserLikedVideo ($resource, DateUtils) {
        var resourceUrl =  'api/user-liked-videos/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.startDateTime = DateUtils.convertDateTimeFromServer(data.startDateTime);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
