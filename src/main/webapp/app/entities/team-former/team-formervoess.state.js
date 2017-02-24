(function() {
    'use strict';

    angular
        .module('voessApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('team-formervoess', {
            parent: 'entity',
            url: '/team-formervoess',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'voessApp.teamFormer.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/team-former/team-formersvoess.html',
                    controller: 'TeamFormerVoessController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('teamFormer');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('team-formervoess-detail', {
            parent: 'entity',
            url: '/team-formervoess/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'voessApp.teamFormer.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/team-former/team-formervoess-detail.html',
                    controller: 'TeamFormerVoessDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('teamFormer');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'TeamFormer', function($stateParams, TeamFormer) {
                    return TeamFormer.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'team-formervoess',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('team-formervoess-detail.edit', {
            parent: 'team-formervoess-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/team-former/team-formervoess-dialog.html',
                    controller: 'TeamFormerVoessDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['TeamFormer', function(TeamFormer) {
                            return TeamFormer.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('team-formervoess.new', {
            parent: 'team-formervoess',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/team-former/team-formervoess-dialog.html',
                    controller: 'TeamFormerVoessDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                startDateTime: null,
                                finshDateTime: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('team-formervoess', null, { reload: 'team-formervoess' });
                }, function() {
                    $state.go('team-formervoess');
                });
            }]
        })
        .state('team-formervoess.edit', {
            parent: 'team-formervoess',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/team-former/team-formervoess-dialog.html',
                    controller: 'TeamFormerVoessDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['TeamFormer', function(TeamFormer) {
                            return TeamFormer.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('team-formervoess', null, { reload: 'team-formervoess' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('team-formervoess.delete', {
            parent: 'team-formervoess',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/team-former/team-formervoess-delete-dialog.html',
                    controller: 'TeamFormerVoessDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['TeamFormer', function(TeamFormer) {
                            return TeamFormer.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('team-formervoess', null, { reload: 'team-formervoess' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
