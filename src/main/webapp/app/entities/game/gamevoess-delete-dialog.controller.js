(function() {
    'use strict';

    angular
        .module('voessApp')
        .controller('GameVoessDeleteController',GameVoessDeleteController);

    GameVoessDeleteController.$inject = ['$uibModalInstance', 'entity', 'Game'];

    function GameVoessDeleteController($uibModalInstance, entity, Game) {
        var vm = this;

        vm.game = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Game.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
