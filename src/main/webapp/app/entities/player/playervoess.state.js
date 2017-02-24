(function() {
    'use strict';

    angular
        .module('voessApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('playervoess', {
            parent: 'entity',
            url: '/playervoess',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'voessApp.player.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/player/playersvoess.html',
                    controller: 'PlayerVoessController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('player');
                    $translatePartialLoader.addPart('sexGender');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('playervoess-detail', {
            parent: 'entity',
            url: '/playervoess/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'voessApp.player.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/player/playervoess-detail.html',
                    controller: 'PlayerVoessDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('player');
                    $translatePartialLoader.addPart('sexGender');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Player', function($stateParams, Player) {
                    return Player.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'playervoess',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('playervoess-detail.edit', {
            parent: 'playervoess-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/player/playervoess-dialog.html',
                    controller: 'PlayerVoessDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Player', function(Player) {
                            return Player.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('playervoess.new', {
            parent: 'playervoess',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/player/playervoess-dialog.html',
                    controller: 'PlayerVoessDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                playerName: null,
                                playerLastName: null,
                                playerNick: null,
                                playerAge: null,
                                playerImage: null,
                                playerImageContentType: null,
                                playerSex: null,
                                playerScore: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('playervoess', null, { reload: 'playervoess' });
                }, function() {
                    $state.go('playervoess');
                });
            }]
        })
        .state('playervoess.edit', {
            parent: 'playervoess',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/player/playervoess-dialog.html',
                    controller: 'PlayerVoessDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Player', function(Player) {
                            return Player.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('playervoess', null, { reload: 'playervoess' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('playervoess.delete', {
            parent: 'playervoess',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/player/playervoess-delete-dialog.html',
                    controller: 'PlayerVoessDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Player', function(Player) {
                            return Player.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('playervoess', null, { reload: 'playervoess' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
