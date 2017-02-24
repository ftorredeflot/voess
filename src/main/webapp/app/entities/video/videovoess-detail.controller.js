(function() {
    'use strict';

    angular
        .module('voessApp')
        .controller('VideoVoessDetailController', VideoVoessDetailController);

    VideoVoessDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Video', 'Game', 'Team', 'Tournament', 'Player'];

    function VideoVoessDetailController($scope, $rootScope, $stateParams, previousState, entity, Video, Game, Team, Tournament, Player) {
        var vm = this;

        vm.video = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('voessApp:videoUpdate', function(event, result) {
            vm.video = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
