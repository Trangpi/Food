(function() {
    'use strict';

    angular
        .module('foodApp')
        .controller('GiohangController', GiohangController);

    GiohangController.$inject = ['$scope', 'Gio_hangs'];

    function GiohangController ($scope, Gio_hangs) {
        var vm = this;
        vm.Bill = Gio_hangs;
        vm.vuducnghia= 'nghia';
    }
})();
