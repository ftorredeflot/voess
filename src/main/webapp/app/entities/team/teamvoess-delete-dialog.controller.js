(function() {
    'use strict';

    angular
        .module('voessApp')
        .controller('TeamVoessDeleteController',TeamVoessDeleteController);

    TeamVoessDeleteController.$inject = ['$uibModalInstance', 'entity', 'Team'];

    function TeamVoessDeleteController($uibModalInstance, entity, Team) {
        var vm = this;

        vm.team = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Team.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
