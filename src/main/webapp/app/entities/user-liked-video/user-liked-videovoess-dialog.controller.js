(function() {
    'use strict';

    angular
        .module('voessApp')
        .controller('UserLikedVideoVoessDialogController', UserLikedVideoVoessDialogController);

    UserLikedVideoVoessDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'UserLikedVideo', 'User', 'Video'];

    function UserLikedVideoVoessDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, UserLikedVideo, User, Video) {
        var vm = this;

        vm.userLikedVideo = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.users = User.query();
        vm.videos = Video.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.userLikedVideo.id !== null) {
                UserLikedVideo.update(vm.userLikedVideo, onSaveSuccess, onSaveError);
            } else {
                UserLikedVideo.save(vm.userLikedVideo, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('voessApp:userLikedVideoUpdate', result);
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
