(function() {
    'use strict';

    angular
        .module('foodApp')
        .controller('BillconectDeleteController',BillconectDeleteController);

    BillconectDeleteController.$inject = ['$uibModalInstance', 'entity', 'Billconect'];

    function BillconectDeleteController($uibModalInstance, entity, Billconect) {
        var vm = this;

        vm.billconect = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Billconect.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
