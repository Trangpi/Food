(function() {
    'use strict';

    angular
        .module('foodApp')
        .controller('BillconectController', BillconectController);

    BillconectController.$inject = ['$scope', '$state', 'Billconect', 'BillconectSearch'];

    function BillconectController ($scope, $state, Billconect, BillconectSearch) {
        var vm = this;
        
        vm.billconects = [];
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            Billconect.query(function(result) {
                vm.billconects = result;
            });
        }

        function search () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            BillconectSearch.query({query: vm.searchQuery}, function(result) {
                vm.billconects = result;
            });
        }    }
})();
