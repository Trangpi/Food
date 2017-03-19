(function() {
    'use strict';

    angular
        .module('foodApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('mon-an', {
            parent: 'entity',
            url: '/mon-an',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'foodApp.monAn.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/mon-an/mon-ans.html',
                    controller: 'MonAnController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('monAn');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('mon-an-detail', {
            parent: 'entity',
            url: '/mon-an/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'foodApp.monAn.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/mon-an/mon-an-detail.html',
                    controller: 'MonAnDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('monAn');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'MonAn', function($stateParams, MonAn) {
                    return MonAn.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'mon-an',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('mon-an-detail.edit', {
            parent: 'mon-an-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/mon-an/mon-an-dialog.html',
                    controller: 'MonAnDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['MonAn', function(MonAn) {
                            return MonAn.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('mon-an.new', {
            parent: 'mon-an',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/mon-an/mon-an-dialog.html',
                    controller: 'MonAnDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                ten: null,
                                gia: null,
                                image: null,
                                imageContentType: null,
                                theloai: null,
                                sale: null,
                                date_create: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('mon-an', null, { reload: 'mon-an' });
                }, function() {
                    $state.go('mon-an');
                });
            }]
        })
        .state('mon-an.edit', {
            parent: 'mon-an',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/mon-an/mon-an-dialog.html',
                    controller: 'MonAnDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['MonAn', function(MonAn) {
                            return MonAn.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('mon-an', null, { reload: 'mon-an' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('mon-an.delete', {
            parent: 'mon-an',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/mon-an/mon-an-delete-dialog.html',
                    controller: 'MonAnDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['MonAn', function(MonAn) {
                            return MonAn.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('mon-an', null, { reload: 'mon-an' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
