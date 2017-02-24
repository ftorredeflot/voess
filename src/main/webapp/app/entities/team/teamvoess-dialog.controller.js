(function() {
    'use strict';

    angular
        .module('voessApp')
        .controller('TeamVoessDialogController', TeamVoessDialogController);

    TeamVoessDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'DataUtils', 'entity', 'Team', 'Video', 'Tournament'];

    function TeamVoessDialogController ($timeout, $scope, $stateParams, $uibModalInstance, DataUtils, entity, Team, Video, Tournament) {
        var vm = this;

        vm.team = entity;
        vm.clear = clear;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;
        vm.save = save;
        vm.videos = Video.query();
        vm.tournaments = Tournament.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.team.id !== null) {
                Team.update(vm.team, onSaveSuccess, onSaveError);
            } else {
                Team.save(vm.team, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('voessApp:teamUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


        vm.setTeamAvatar = function ($file, team) {
            if ($file && $file.$error === 'pattern') {
                return;
            }
            if ($file) {
                DataUtils.toBase64($file, function(base64Data) {
                    $scope.$apply(function() {
                        team.teamAvatar = base64Data;
                        team.teamAvatarContentType = $file.type;
                    });
                });
            }
        };

    }
})();
