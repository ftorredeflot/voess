(function() {
    'use strict';

    angular
        .module('voessApp')
        .controller('CountryVoessDeleteController',CountryVoessDeleteController);

    CountryVoessDeleteController.$inject = ['$uibModalInstance', 'entity', 'Country'];

    function CountryVoessDeleteController($uibModalInstance, entity, Country) {
        var vm = this;

        vm.country = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Country.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
