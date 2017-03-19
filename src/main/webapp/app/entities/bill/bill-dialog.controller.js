(function() {
    'use strict';

    angular
        .module('foodApp')
        .controller('BillDialogController', BillDialogController);

    BillDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Bill', 'Billconect', 'User'];

    function BillDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Bill, Billconect, User) {
        var vm = this;

        vm.bill = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.billconects = Billconect.query();
        vm.users = User.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.bill.id !== null) {
                Bill.update(vm.bill, onSaveSuccess, onSaveError);
            } else {
                Bill.save(vm.bill, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('foodApp:billUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.date = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
