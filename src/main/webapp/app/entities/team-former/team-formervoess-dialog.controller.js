(function() {
    'use strict';

    angular
        .module('voessApp')
        .controller('TeamFormerVoessDialogController', TeamFormerVoessDialogController);

    TeamFormerVoessDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'TeamFormer', 'Player', 'Team'];

    function TeamFormerVoessDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, TeamFormer, Player, Team) {
        var vm = this;

        vm.teamFormer = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.players = Player.query();
        vm.teams = Team.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.teamFormer.id !== null) {
                TeamFormer.update(vm.teamFormer, onSaveSuccess, onSaveError);
            } else {
                TeamFormer.save(vm.teamFormer, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('voessApp:teamFormerUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.startDateTime = false;
        vm.datePickerOpenStatus.finshDateTime = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
