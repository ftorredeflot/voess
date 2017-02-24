(function() {
    'use strict';

    angular
        .module('voessApp')
        .controller('UserLikedPlayerVoessDetailController', UserLikedPlayerVoessDetailController);

    UserLikedPlayerVoessDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'UserLikedPlayer', 'User', 'Player'];

    function UserLikedPlayerVoessDetailController($scope, $rootScope, $stateParams, previousState, entity, UserLikedPlayer, User, Player) {
        var vm = this;

        vm.userLikedPlayer = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('voessApp:userLikedPlayerUpdate', function(event, result) {
            vm.userLikedPlayer = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
