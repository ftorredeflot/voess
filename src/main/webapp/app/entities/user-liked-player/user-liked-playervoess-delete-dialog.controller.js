(function() {
    'use strict';

    angular
        .module('voessApp')
        .controller('UserLikedPlayerVoessDeleteController',UserLikedPlayerVoessDeleteController);

    UserLikedPlayerVoessDeleteController.$inject = ['$uibModalInstance', 'entity', 'UserLikedPlayer'];

    function UserLikedPlayerVoessDeleteController($uibModalInstance, entity, UserLikedPlayer) {
        var vm = this;

        vm.userLikedPlayer = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            UserLikedPlayer.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
