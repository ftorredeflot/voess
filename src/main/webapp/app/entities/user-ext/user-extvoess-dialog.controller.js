(function() {
    'use strict';

    angular
        .module('voessApp')
        .controller('UserExtVoessDialogController', UserExtVoessDialogController);

    UserExtVoessDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'DataUtils', 'entity', 'UserExt', 'User'];

    function UserExtVoessDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, DataUtils, entity, UserExt, User) {
        var vm = this;

        vm.userExt = entity;
        vm.clear = clear;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;
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
            if (vm.userExt.id !== null) {
                UserExt.update(vm.userExt, onSaveSuccess, onSaveError);
            } else {
                UserExt.save(vm.userExt, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('voessApp:userExtUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


        vm.setUserImage = function ($file, userExt) {
            if ($file && $file.$error === 'pattern') {
                return;
            }
            if ($file) {
                DataUtils.toBase64($file, function(base64Data) {
                    $scope.$apply(function() {
                        userExt.userImage = base64Data;
                        userExt.userImageContentType = $file.type;
                    });
                });
            }
        };

    }
})();
