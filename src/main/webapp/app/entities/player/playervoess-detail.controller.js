(function() {
    'use strict';

    angular
        .module('voessApp')
        .controller('PlayerVoessDetailController', PlayerVoessDetailController);

    PlayerVoessDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'DataUtils', 'entity', 'Player', 'Country', 'Video'];

    function PlayerVoessDetailController($scope, $rootScope, $stateParams, previousState, DataUtils, entity, Player, Country, Video) {
        var vm = this;

        vm.player = entity;
        vm.previousState = previousState.name;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;

        var unsubscribe = $rootScope.$on('voessApp:playerUpdate', function(event, result) {
            vm.player = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
