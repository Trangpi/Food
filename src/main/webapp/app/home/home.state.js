(function() {
    'use strict';

    angular
        .module('foodApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider.state('home', {
            parent: 'app',
            url: '/',
            data: {
                authorities: []
            },
            views: {
                'content@': {
                    templateUrl: 'app/home/home.html',
                    controller: 'HomeController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                mainTranslatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate,$translatePartialLoader) {
                    $translatePartialLoader.addPart('home');
                    return $translate.refresh();
                }]
            }
        })
        .state('gio_hang', {
            parent: 'home',
            url: 'gio-hang',
            data: {
                authorities: []
            },
            views: {
                'content@': {
                    templateUrl: 'app/home/gio_hang.html',
                    controller: 'GiohangController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                Gio_hangs : ['Bill', function(Bill){
                    return Bill.query().$promise;
                }],
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate,$translatePartialLoader) {
                    $translatePartialLoader.addPart('home');
                    return $translate.refresh();
                }]
            }
        });
    }
})();
