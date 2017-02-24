(function() {
    'use strict';

    angular
        .module('voessApp')
        .controller('TeamVoessDetailController', TeamVoessDetailController);

    TeamVoessDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'DataUtils', 'entity', 'Team', 'Video', 'Tournament'];

    function TeamVoessDetailController($scope, $rootScope, $stateParams, previousState, DataUtils, entity, Team, Video, Tournament) {
        var vm = this;

        vm.team = entity;
        vm.previousState = previousState.name;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;

        var unsubscribe = $rootScope.$on('voessApp:teamUpdate', function(event, result) {
            vm.team = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
