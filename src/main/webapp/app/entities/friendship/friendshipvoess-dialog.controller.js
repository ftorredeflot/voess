(function() {
    'use strict';

    angular
        .module('voessApp')
        .controller('FriendshipVoessDialogController', FriendshipVoessDialogController);

    FriendshipVoessDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Friendship', 'User'];

    function FriendshipVoessDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Friendship, User) {
        var vm = this;

        vm.friendship = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.users = User.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.friendship.id !== null) {
                Friendship.update(vm.friendship, onSaveSuccess, onSaveError);
            } else {
                Friendship.save(vm.friendship, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('voessApp:friendshipUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.startDateTime = false;
        vm.datePickerOpenStatus.finishDateTime = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
