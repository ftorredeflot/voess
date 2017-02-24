(function() {
    'use strict';
    angular
        .module('voessApp')
        .factory('Tournament', Tournament);

    Tournament.$inject = ['$resource', 'DateUtils'];

    function Tournament ($resource, DateUtils) {
        var resourceUrl =  'api/tournaments/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.tournamentDate = DateUtils.convertDateTimeFromServer(data.tournamentDate);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
