(function() {
    'use strict';
    angular
        .module('voessApp')
        .factory('TeamFormer', TeamFormer);

    TeamFormer.$inject = ['$resource', 'DateUtils'];

    function TeamFormer ($resource, DateUtils) {
        var resourceUrl =  'api/team-formers/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.startDateTime = DateUtils.convertDateTimeFromServer(data.startDateTime);
                        data.finshDateTime = DateUtils.convertDateTimeFromServer(data.finshDateTime);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
