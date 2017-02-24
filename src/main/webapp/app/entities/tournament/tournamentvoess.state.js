(function() {
    'use strict';

    angular
        .module('voessApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('tournamentvoess', {
            parent: 'entity',
            url: '/tournamentvoess',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'voessApp.tournament.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/tournament/tournamentsvoess.html',
                    controller: 'TournamentVoessController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('tournament');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('tournamentvoess-detail', {
            parent: 'entity',
            url: '/tournamentvoess/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'voessApp.tournament.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/tournament/tournamentvoess-detail.html',
                    controller: 'TournamentVoessDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('tournament');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Tournament', function($stateParams, Tournament) {
                    return Tournament.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'tournamentvoess',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('tournamentvoess-detail.edit', {
            parent: 'tournamentvoess-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/tournament/tournamentvoess-dialog.html',
                    controller: 'TournamentVoessDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Tournament', function(Tournament) {
                            return Tournament.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('tournamentvoess.new', {
            parent: 'tournamentvoess',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/tournament/tournamentvoess-dialog.html',
                    controller: 'TournamentVoessDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                tournamentName: null,
                                tournamentDate: null,
                                tournamentImage: null,
                                tournamentImageContentType: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('tournamentvoess', null, { reload: 'tournamentvoess' });
                }, function() {
                    $state.go('tournamentvoess');
                });
            }]
        })
        .state('tournamentvoess.edit', {
            parent: 'tournamentvoess',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/tournament/tournamentvoess-dialog.html',
                    controller: 'TournamentVoessDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Tournament', function(Tournament) {
                            return Tournament.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('tournamentvoess', null, { reload: 'tournamentvoess' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('tournamentvoess.delete', {
            parent: 'tournamentvoess',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/tournament/tournamentvoess-delete-dialog.html',
                    controller: 'TournamentVoessDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Tournament', function(Tournament) {
                            return Tournament.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('tournamentvoess', null, { reload: 'tournamentvoess' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
