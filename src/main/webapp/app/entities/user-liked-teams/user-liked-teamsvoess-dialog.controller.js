(function() {
    'use strict';

    angular
        .module('voessApp')
        .controller('UserLikedTeamsVoessDialogController', UserLikedTeamsVoessDialogController);

    UserLikedTeamsVoessDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'UserLikedTeams', 'User', 'Team'];

    function UserLikedTeamsVoessDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, UserLikedTeams, User, Team) {
        var vm = this;

        vm.userLikedTeams = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.users = User.query();
        vm.teams = Team.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.userLikedTeams.id !== null) {
                UserLikedTeams.update(vm.userLikedTeams, onSaveSuccess, onSaveError);
            } else {
                UserLikedTeams.save(vm.userLikedTeams, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('voessApp:userLikedTeamsUpdate', result);
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
