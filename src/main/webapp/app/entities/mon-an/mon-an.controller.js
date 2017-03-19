(function() {
    'use strict';

    angular
        .module('foodApp')
        .controller('MonAnController', MonAnController);

    MonAnController.$inject = ['$scope', '$state', 'DataUtils', 'MonAn', 'MonAnSearch'];

    function MonAnController ($scope, $state, DataUtils, MonAn, MonAnSearch) {
        var vm = this;
        
        vm.monAns = [];
        vm.openFile = DataUtils.openFile;
        vm.byteSize = DataUtils.byteSize;
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            MonAn.query(function(result) {
                vm.monAns = result;
            });
        }

        function search () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            MonAnSearch.query({query: vm.searchQuery}, function(result) {
                vm.monAns = result;
            });
        }    }
})();
