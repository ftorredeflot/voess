(function() {
    'use strict';

    angular
        .module('voessApp')
        .controller('UserExtVoessDetailController', UserExtVoessDetailController);

    UserExtVoessDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'DataUtils', 'entity', 'UserExt', 'User'];

    function UserExtVoessDetailController($scope, $rootScope, $stateParams, previousState, DataUtils, entity, UserExt, User) {
        var vm = this;

        vm.userExt = entity;
        vm.previousState = previousState.name;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;

        var unsubscribe = $rootScope.$on('voessApp:userExtUpdate', function(event, result) {
            vm.userExt = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
