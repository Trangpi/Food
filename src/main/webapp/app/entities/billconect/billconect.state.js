(function() {
    'use strict';

    angular
        .module('foodApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('billconect', {
            parent: 'entity',
            url: '/billconect',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'foodApp.billconect.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/billconect/billconects.html',
                    controller: 'BillconectController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('billconect');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('billconect-detail', {
            parent: 'entity',
            url: '/billconect/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'foodApp.billconect.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/billconect/billconect-detail.html',
                    controller: 'BillconectDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('billconect');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Billconect', function($stateParams, Billconect) {
                    return Billconect.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'billconect',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('billconect-detail.edit', {
            parent: 'billconect-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/billconect/billconect-dialog.html',
                    controller: 'BillconectDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Billconect', function(Billconect) {
                            return Billconect.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('billconect.new', {
            parent: 'billconect',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/billconect/billconect-dialog.html',
                    controller: 'BillconectDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                soluong: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('billconect', null, { reload: 'billconect' });
                }, function() {
                    $state.go('billconect');
                });
            }]
        })
        .state('billconect.edit', {
            parent: 'billconect',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/billconect/billconect-dialog.html',
                    controller: 'BillconectDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Billconect', function(Billconect) {
                            return Billconect.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('billconect', null, { reload: 'billconect' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('billconect.delete', {
            parent: 'billconect',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/billconect/billconect-delete-dialog.html',
                    controller: 'BillconectDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Billconect', function(Billconect) {
                            return Billconect.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('billconect', null, { reload: 'billconect' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
