(function() {
    'use strict';

    angular
        .module('voessApp')
        .controller('PlayerVoessDeleteController',PlayerVoessDeleteController);

    PlayerVoessDeleteController.$inject = ['$uibModalInstance', 'entity', 'Player'];

    function PlayerVoessDeleteController($uibModalInstance, entity, Player) {
        var vm = this;

        vm.player = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Player.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
