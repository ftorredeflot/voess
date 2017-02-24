(function() {
    'use strict';

    angular
        .module('voessApp')
        .controller('UserLikedVideoVoessDeleteController',UserLikedVideoVoessDeleteController);

    UserLikedVideoVoessDeleteController.$inject = ['$uibModalInstance', 'entity', 'UserLikedVideo'];

    function UserLikedVideoVoessDeleteController($uibModalInstance, entity, UserLikedVideo) {
        var vm = this;

        vm.userLikedVideo = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            UserLikedVideo.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
