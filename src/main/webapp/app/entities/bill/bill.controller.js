(function() {
    'use strict';

    angular
        .module('foodApp')
        .controller('BillController', BillController);

    BillController.$inject = ['$scope', '$state', 'Bill', 'BillSearch'];

    function BillController ($scope, $state, Bill, BillSearch) {
        var vm = this;
        
        vm.bills = [];
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            Bill.query(function(result) {
                vm.bills = result;
            });
        }

        function search () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            BillSearch.query({query: vm.searchQuery}, function(result) {
                vm.bills = result;
            });
        }    }
})();
