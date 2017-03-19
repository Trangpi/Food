(function() {
    'use strict';

    angular
        .module('foodApp')
        .controller('BillconectDetailController', BillconectDetailController);

    BillconectDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Billconect', 'MonAn', 'Bill'];

    function BillconectDetailController($scope, $rootScope, $stateParams, previousState, entity, Billconect, MonAn, Bill) {
        var vm = this;

        vm.billconect = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('foodApp:billconectUpdate', function(event, result) {
            vm.billconect = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
