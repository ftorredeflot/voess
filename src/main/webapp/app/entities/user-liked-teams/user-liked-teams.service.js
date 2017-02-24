(function() {
    'use strict';
    angular
        .module('voessApp')
        .factory('UserLikedTeams', UserLikedTeams);

    UserLikedTeams.$inject = ['$resource', 'DateUtils'];

    function UserLikedTeams ($resource, DateUtils) {
        var resourceUrl =  'api/user-liked-teams/:id';

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
