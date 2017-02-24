(function() {
    'use strict';

    angular
        .module('voessApp')
        .controller('FriendshipVoessDeleteController',FriendshipVoessDeleteController);

    FriendshipVoessDeleteController.$inject = ['$uibModalInstance', 'entity', 'Friendship'];

    function FriendshipVoessDeleteController($uibModalInstance, entity, Friendship) {
        var vm = this;

        vm.friendship = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Friendship.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
