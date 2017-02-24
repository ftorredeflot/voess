(function() {
    'use strict';

    angular
        .module('voessApp')
        .controller('UserLikedVideoVoessDetailController', UserLikedVideoVoessDetailController);

    UserLikedVideoVoessDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'UserLikedVideo', 'User', 'Video'];

    function UserLikedVideoVoessDetailController($scope, $rootScope, $stateParams, previousState, entity, UserLikedVideo, User, Video) {
        var vm = this;

        vm.userLikedVideo = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('voessApp:userLikedVideoUpdate', function(event, result) {
            vm.userLikedVideo = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
