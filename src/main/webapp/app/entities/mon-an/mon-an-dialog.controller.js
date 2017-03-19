(function() {
    'use strict';

    angular
        .module('foodApp')
        .controller('MonAnDialogController', MonAnDialogController);

    MonAnDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'DataUtils', 'entity', 'MonAn', 'Billconect'];

    function MonAnDialogController ($timeout, $scope, $stateParams, $uibModalInstance, DataUtils, entity, MonAn, Billconect) {
        var vm = this;

        vm.monAn = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;
        vm.save = save;
        vm.billconects = Billconect.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.monAn.id !== null) {
                MonAn.update(vm.monAn, onSaveSuccess, onSaveError);
            } else {
                MonAn.save(vm.monAn, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('foodApp:monAnUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


        vm.setImage = function ($file, monAn) {
            if ($file && $file.$error === 'pattern') {
                return;
            }
            if ($file) {
                DataUtils.toBase64($file, function(base64Data) {
                    $scope.$apply(function() {
                        monAn.image = base64Data;
                        monAn.imageContentType = $file.type;
                    });
                });
            }
        };
        vm.datePickerOpenStatus.date_create = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
