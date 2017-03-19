(function() {
    'use strict';

    angular
        .module('foodApp')
        .controller('BillDeleteController',BillDeleteController);

    BillDeleteController.$inject = ['$uibModalInstance', 'entity', 'Bill'];

    function BillDeleteController($uibModalInstance, entity, Bill) {
        var vm = this;

        vm.bill = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Bill.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
