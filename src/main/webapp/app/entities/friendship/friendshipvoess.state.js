(function() {
    'use strict';

    angular
        .module('voessApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('friendshipvoess', {
            parent: 'entity',
            url: '/friendshipvoess',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'voessApp.friendship.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/friendship/friendshipsvoess.html',
                    controller: 'FriendshipVoessController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('friendship');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('friendshipvoess-detail', {
            parent: 'entity',
            url: '/friendshipvoess/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'voessApp.friendship.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/friendship/friendshipvoess-detail.html',
                    controller: 'FriendshipVoessDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('friendship');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Friendship', function($stateParams, Friendship) {
                    return Friendship.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'friendshipvoess',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('friendshipvoess-detail.edit', {
            parent: 'friendshipvoess-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/friendship/friendshipvoess-dialog.html',
                    controller: 'FriendshipVoessDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Friendship', function(Friendship) {
                            return Friendship.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('friendshipvoess.new', {
            parent: 'friendshipvoess',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/friendship/friendshipvoess-dialog.html',
                    controller: 'FriendshipVoessDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                startDateTime: null,
                                finishDateTime: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('friendshipvoess', null, { reload: 'friendshipvoess' });
                }, function() {
                    $state.go('friendshipvoess');
                });
            }]
        })
        .state('friendshipvoess.edit', {
            parent: 'friendshipvoess',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/friendship/friendshipvoess-dialog.html',
                    controller: 'FriendshipVoessDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Friendship', function(Friendship) {
                            return Friendship.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('friendshipvoess', null, { reload: 'friendshipvoess' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('friendshipvoess.delete', {
            parent: 'friendshipvoess',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/friendship/friendshipvoess-delete-dialog.html',
                    controller: 'FriendshipVoessDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Friendship', function(Friendship) {
                            return Friendship.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('friendshipvoess', null, { reload: 'friendshipvoess' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
