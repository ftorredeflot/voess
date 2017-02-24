(function() {
    'use strict';

    angular
        .module('voessApp')
        .controller('TeamFormerVoessDetailController', TeamFormerVoessDetailController);

    TeamFormerVoessDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'TeamFormer', 'Player', 'Team'];

    function TeamFormerVoessDetailController($scope, $rootScope, $stateParams, previousState, entity, TeamFormer, Player, Team) {
        var vm = this;

        vm.teamFormer = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('voessApp:teamFormerUpdate', function(event, result) {
            vm.teamFormer = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
