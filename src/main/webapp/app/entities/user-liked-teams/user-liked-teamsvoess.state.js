(function() {
    'use strict';

    angular
        .module('voessApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('user-liked-teamsvoess', {
            parent: 'entity',
            url: '/user-liked-teamsvoess',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'voessApp.userLikedTeams.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/user-liked-teams/user-liked-teamsvoess.html',
                    controller: 'UserLikedTeamsVoessController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('userLikedTeams');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('user-liked-teamsvoess-detail', {
            parent: 'entity',
            url: '/user-liked-teamsvoess/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'voessApp.userLikedTeams.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/user-liked-teams/user-liked-teamsvoess-detail.html',
                    controller: 'UserLikedTeamsVoessDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('userLikedTeams');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'UserLikedTeams', function($stateParams, UserLikedTeams) {
                    return UserLikedTeams.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'user-liked-teamsvoess',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('user-liked-teamsvoess-detail.edit', {
            parent: 'user-liked-teamsvoess-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/user-liked-teams/user-liked-teamsvoess-dialog.html',
                    controller: 'UserLikedTeamsVoessDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['UserLikedTeams', function(UserLikedTeams) {
                            return UserLikedTeams.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('user-liked-teamsvoess.new', {
            parent: 'user-liked-teamsvoess',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/user-liked-teams/user-liked-teamsvoess-dialog.html',
                    controller: 'UserLikedTeamsVoessDialogController',
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
                    $state.go('user-liked-teamsvoess', null, { reload: 'user-liked-teamsvoess' });
                }, function() {
                    $state.go('user-liked-teamsvoess');
                });
            }]
        })
        .state('user-liked-teamsvoess.edit', {
            parent: 'user-liked-teamsvoess',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/user-liked-teams/user-liked-teamsvoess-dialog.html',
                    controller: 'UserLikedTeamsVoessDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['UserLikedTeams', function(UserLikedTeams) {
                            return UserLikedTeams.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('user-liked-teamsvoess', null, { reload: 'user-liked-teamsvoess' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('user-liked-teamsvoess.delete', {
            parent: 'user-liked-teamsvoess',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/user-liked-teams/user-liked-teamsvoess-delete-dialog.html',
                    controller: 'UserLikedTeamsVoessDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['UserLikedTeams', function(UserLikedTeams) {
                            return UserLikedTeams.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('user-liked-teamsvoess', null, { reload: 'user-liked-teamsvoess' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
