(function() {
    'use strict';

    angular
        .module('voessApp')
        .controller('TeamFormerVoessDeleteController',TeamFormerVoessDeleteController);

    TeamFormerVoessDeleteController.$inject = ['$uibModalInstance', 'entity', 'TeamFormer'];

    function TeamFormerVoessDeleteController($uibModalInstance, entity, TeamFormer) {
        var vm = this;

        vm.teamFormer = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            TeamFormer.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
