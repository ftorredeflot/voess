(function() {
    'use strict';

    angular
        .module('voessApp')
        .controller('CountryVoessDetailController', CountryVoessDetailController);

    CountryVoessDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'DataUtils', 'entity', 'Country', 'Player'];

    function CountryVoessDetailController($scope, $rootScope, $stateParams, previousState, DataUtils, entity, Country, Player) {
        var vm = this;

        vm.country = entity;
        vm.previousState = previousState.name;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;

        var unsubscribe = $rootScope.$on('voessApp:countryUpdate', function(event, result) {
            vm.country = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
