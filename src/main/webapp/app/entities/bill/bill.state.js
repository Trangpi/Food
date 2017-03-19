(function() {
    'use strict';

    angular
        .module('foodApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('bill', {
            parent: 'entity',
            url: '/bill',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'foodApp.bill.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/bill/bills.html',
                    controller: 'BillController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('bill');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('bill-detail', {
            parent: 'entity',
            url: '/bill/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'foodApp.bill.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/bill/bill-detail.html',
                    controller: 'BillDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('bill');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Bill', function($stateParams, Bill) {
                    return Bill.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'bill',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('bill-detail.edit', {
            parent: 'bill-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/bill/bill-dialog.html',
                    controller: 'BillDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Bill', function(Bill) {
                            return Bill.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('bill.new', {
            parent: 'bill',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/bill/bill-dialog.html',
                    controller: 'BillDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                ship: null,
                                date: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('bill', null, { reload: 'bill' });
                }, function() {
                    $state.go('bill');
                });
            }]
        })
        .state('bill.edit', {
            parent: 'bill',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/bill/bill-dialog.html',
                    controller: 'BillDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Bill', function(Bill) {
                            return Bill.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('bill', null, { reload: 'bill' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('bill.delete', {
            parent: 'bill',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/bill/bill-delete-dialog.html',
                    controller: 'BillDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Bill', function(Bill) {
                            return Bill.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('bill', null, { reload: 'bill' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
