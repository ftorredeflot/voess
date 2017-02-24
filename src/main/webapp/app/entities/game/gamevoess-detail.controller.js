(function() {
    'use strict';

    angular
        .module('voessApp')
        .controller('GameVoessDetailController', GameVoessDetailController);

    GameVoessDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'DataUtils', 'entity', 'Game', 'Video'];

    function GameVoessDetailController($scope, $rootScope, $stateParams, previousState, DataUtils, entity, Game, Video) {
        var vm = this;

        vm.game = entity;
        vm.previousState = previousState.name;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;

        var unsubscribe = $rootScope.$on('voessApp:gameUpdate', function(event, result) {
            vm.game = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
