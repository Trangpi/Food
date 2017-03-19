(function() {
    'use strict';

    angular
        .module('foodApp')
        .controller('BillDetailController', BillDetailController);

    BillDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Bill', 'Billconect', 'User'];

    function BillDetailController($scope, $rootScope, $stateParams, previousState, entity, Bill, Billconect, User) {
        var vm = this;

        vm.bill = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('foodApp:billUpdate', function(event, result) {
            vm.bill = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
