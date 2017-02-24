(function() {
    'use strict';

    angular
        .module('voessApp')
        .controller('UserLikedTeamsVoessDetailController', UserLikedTeamsVoessDetailController);

    UserLikedTeamsVoessDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'UserLikedTeams', 'User', 'Team'];

    function UserLikedTeamsVoessDetailController($scope, $rootScope, $stateParams, previousState, entity, UserLikedTeams, User, Team) {
        var vm = this;

        vm.userLikedTeams = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('voessApp:userLikedTeamsUpdate', function(event, result) {
            vm.userLikedTeams = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
