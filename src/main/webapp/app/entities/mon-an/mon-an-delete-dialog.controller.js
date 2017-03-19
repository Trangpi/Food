(function() {
    'use strict';

    angular
        .module('foodApp')
        .controller('MonAnDeleteController',MonAnDeleteController);

    MonAnDeleteController.$inject = ['$uibModalInstance', 'entity', 'MonAn'];

    function MonAnDeleteController($uibModalInstance, entity, MonAn) {
        var vm = this;

        vm.monAn = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            MonAn.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
