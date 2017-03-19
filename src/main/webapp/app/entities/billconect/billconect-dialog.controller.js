(function() {
    'use strict';

    angular
        .module('foodApp')
        .controller('BillconectDialogController', BillconectDialogController);

    BillconectDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Billconect', 'MonAn', 'Bill'];

    function BillconectDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Billconect, MonAn, Bill) {
        var vm = this;

        vm.billconect = entity;
        vm.clear = clear;
        vm.save = save;
        vm.monans = MonAn.query();
        vm.bills = Bill.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.billconect.id !== null) {
                Billconect.update(vm.billconect, onSaveSuccess, onSaveError);
            } else {
                Billconect.save(vm.billconect, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('foodApp:billconectUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
