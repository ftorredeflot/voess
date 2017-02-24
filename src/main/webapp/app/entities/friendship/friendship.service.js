(function() {
    'use strict';
    angular
        .module('voessApp')
        .factory('Friendship', Friendship);

    Friendship.$inject = ['$resource', 'DateUtils'];

    function Friendship ($resource, DateUtils) {
        var resourceUrl =  'api/friendships/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.startDateTime = DateUtils.convertDateTimeFromServer(data.startDateTime);
                        data.finishDateTime = DateUtils.convertDateTimeFromServer(data.finishDateTime);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
