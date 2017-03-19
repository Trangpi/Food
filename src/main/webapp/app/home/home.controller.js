(function() {
    'use strict';

    angular
        .module('foodApp')
        .controller('HomeController', HomeController);

    HomeController.$inject = ['$scope', 'Principal', 'LoginService', '$state','MonAn'];

    function HomeController ($scope, Principal, LoginService, $state, MonAn) {
        var vm = this;

        vm.account = null;
        vm.isAuthenticated = null;
        vm.login = LoginService.open;
        vm.register = register;

        vm.nghia='nghia';
        vm.monAns = [];

        loadAll();

        function loadAll() {
            MonAn.query(function(result) {

                vm.monAns = result;
                vm.searchQuery = null;
            });
        }

        $scope.$on('authenticationSuccess', function() {
            getAccount();
        });

        getAccount();

        function getAccount() {
            Principal.identity().then(function(account) {
                vm.account = account;
                vm.isAuthenticated = Principal.isAuthenticated;
            });
        }
        function register () {
            $state.go('register');
        }

        function register(){
           $state.go('resgister');
        }

        function logout(){
           collapseNavbar();
           Au
        }




    }
})();
