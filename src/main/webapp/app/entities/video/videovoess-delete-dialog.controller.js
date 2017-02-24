(function() {
    'use strict';

    angular
        .module('voessApp')
        .controller('VideoVoessDeleteController',VideoVoessDeleteController);

    VideoVoessDeleteController.$inject = ['$uibModalInstance', 'entity', 'Video'];

    function VideoVoessDeleteController($uibModalInstance, entity, Video) {
        var vm = this;

        vm.video = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Video.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
