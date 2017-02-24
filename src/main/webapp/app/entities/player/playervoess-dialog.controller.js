(function() {
    'use strict';

    angular
        .module('voessApp')
        .controller('PlayerVoessDialogController', PlayerVoessDialogController);

    PlayerVoessDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'DataUtils', 'entity', 'Player', 'Country', 'Video'];

    function PlayerVoessDialogController ($timeout, $scope, $stateParams, $uibModalInstance, DataUtils, entity, Player, Country, Video) {
        var vm = this;

        vm.player = entity;
        vm.clear = clear;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;
        vm.save = save;
        vm.countries = Country.query();
        vm.videos = Video.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.player.id !== null) {
                Player.update(vm.player, onSaveSuccess, onSaveError);
            } else {
                Player.save(vm.player, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('voessApp:playerUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


        vm.setPlayerImage = function ($file, player) {
            if ($file && $file.$error === 'pattern') {
                return;
            }
            if ($file) {
                DataUtils.toBase64($file, function(base64Data) {
                    $scope.$apply(function() {
                        player.playerImage = base64Data;
                        player.playerImageContentType = $file.type;
                    });
                });
            }
        };

    }
})();
