(function() {
    'use strict';
    angular
        .module('voessApp')
        .factory('UserLikedPlayer', UserLikedPlayer);

    UserLikedPlayer.$inject = ['$resource', 'DateUtils'];

    function UserLikedPlayer ($resource, DateUtils) {
        var resourceUrl =  'api/user-liked-players/:id';

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
