(function() {
    'use strict';

    angular
        .module('voessApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('user-liked-playervoess', {
            parent: 'entity',
            url: '/user-liked-playervoess',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'voessApp.userLikedPlayer.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/user-liked-player/user-liked-playersvoess.html',
                    controller: 'UserLikedPlayerVoessController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('userLikedPlayer');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('user-liked-playervoess-detail', {
            parent: 'entity',
            url: '/user-liked-playervoess/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'voessApp.userLikedPlayer.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/user-liked-player/user-liked-playervoess-detail.html',
                    controller: 'UserLikedPlayerVoessDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('userLikedPlayer');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'UserLikedPlayer', function($stateParams, UserLikedPlayer) {
                    return UserLikedPlayer.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'user-liked-playervoess',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('user-liked-playervoess-detail.edit', {
            parent: 'user-liked-playervoess-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/user-liked-player/user-liked-playervoess-dialog.html',
                    controller: 'UserLikedPlayerVoessDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['UserLikedPlayer', function(UserLikedPlayer) {
                            return UserLikedPlayer.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('user-liked-playervoess.new', {
            parent: 'user-liked-playervoess',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/user-liked-player/user-liked-playervoess-dialog.html',
                    controller: 'UserLikedPlayerVoessDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                startDateTime: null,
                                userLiked: false,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('user-liked-playervoess', null, { reload: 'user-liked-playervoess' });
                }, function() {
                    $state.go('user-liked-playervoess');
                });
            }]
        })
        .state('user-liked-playervoess.edit', {
            parent: 'user-liked-playervoess',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/user-liked-player/user-liked-playervoess-dialog.html',
                    controller: 'UserLikedPlayerVoessDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['UserLikedPlayer', function(UserLikedPlayer) {
                            return UserLikedPlayer.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('user-liked-playervoess', null, { reload: 'user-liked-playervoess' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('user-liked-playervoess.delete', {
            parent: 'user-liked-playervoess',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/user-liked-player/user-liked-playervoess-delete-dialog.html',
                    controller: 'UserLikedPlayerVoessDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['UserLikedPlayer', function(UserLikedPlayer) {
                            return UserLikedPlayer.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('user-liked-playervoess', null, { reload: 'user-liked-playervoess' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
