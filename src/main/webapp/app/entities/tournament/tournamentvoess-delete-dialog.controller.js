(function() {
    'use strict';

    angular
        .module('voessApp')
        .controller('TournamentVoessDeleteController',TournamentVoessDeleteController);

    TournamentVoessDeleteController.$inject = ['$uibModalInstance', 'entity', 'Tournament'];

    function TournamentVoessDeleteController($uibModalInstance, entity, Tournament) {
        var vm = this;

        vm.tournament = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Tournament.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
