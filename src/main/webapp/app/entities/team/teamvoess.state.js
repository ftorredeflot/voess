(function() {
    'use strict';

    angular
        .module('voessApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('teamvoess', {
            parent: 'entity',
            url: '/teamvoess',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'voessApp.team.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/team/teamsvoess.html',
                    controller: 'TeamVoessController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('team');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('teamvoess-detail', {
            parent: 'entity',
            url: '/teamvoess/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'voessApp.team.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/team/teamvoess-detail.html',
                    controller: 'TeamVoessDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('team');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Team', function($stateParams, Team) {
                    return Team.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'teamvoess',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('teamvoess-detail.edit', {
            parent: 'teamvoess-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/team/teamvoess-dialog.html',
                    controller: 'TeamVoessDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Team', function(Team) {
                            return Team.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('teamvoess.new', {
            parent: 'teamvoess',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/team/teamvoess-dialog.html',
                    controller: 'TeamVoessDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                teamName: null,
                                teamAvatar: null,
                                teamAvatarContentType: null,
                                teamWin: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('teamvoess', null, { reload: 'teamvoess' });
                }, function() {
                    $state.go('teamvoess');
                });
            }]
        })
        .state('teamvoess.edit', {
            parent: 'teamvoess',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/team/teamvoess-dialog.html',
                    controller: 'TeamVoessDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Team', function(Team) {
                            return Team.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('teamvoess', null, { reload: 'teamvoess' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('teamvoess.delete', {
            parent: 'teamvoess',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/team/teamvoess-delete-dialog.html',
                    controller: 'TeamVoessDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Team', function(Team) {
                            return Team.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('teamvoess', null, { reload: 'teamvoess' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
