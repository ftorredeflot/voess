(function() {
    'use strict';

    angular
        .module('voessApp')
        .controller('UserLikedPlayerVoessDialogController', UserLikedPlayerVoessDialogController);

    UserLikedPlayerVoessDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'UserLikedPlayer', 'User', 'Player'];

    function UserLikedPlayerVoessDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, UserLikedPlayer, User, Player) {
        var vm = this;

        vm.userLikedPlayer = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.users = User.query();
        vm.players = Player.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.userLikedPlayer.id !== null) {
                UserLikedPlayer.update(vm.userLikedPlayer, onSaveSuccess, onSaveError);
            } else {
                UserLikedPlayer.save(vm.userLikedPlayer, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('voessApp:userLikedPlayerUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.startDateTime = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
