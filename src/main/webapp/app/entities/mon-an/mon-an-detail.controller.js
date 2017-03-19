(function() {
    'use strict';

    angular
        .module('foodApp')
        .controller('MonAnDetailController', MonAnDetailController);

    MonAnDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'DataUtils', 'entity', 'MonAn', 'Billconect'];

    function MonAnDetailController($scope, $rootScope, $stateParams, previousState, DataUtils, entity, MonAn, Billconect) {
        var vm = this;

        vm.monAn = entity;
        vm.previousState = previousState.name;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;

        var unsubscribe = $rootScope.$on('foodApp:monAnUpdate', function(event, result) {
            vm.monAn = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
