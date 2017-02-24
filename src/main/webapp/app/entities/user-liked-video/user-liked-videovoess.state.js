(function() {
    'use strict';

    angular
        .module('voessApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('user-liked-videovoess', {
            parent: 'entity',
            url: '/user-liked-videovoess',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'voessApp.userLikedVideo.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/user-liked-video/user-liked-videosvoess.html',
                    controller: 'UserLikedVideoVoessController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('userLikedVideo');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('user-liked-videovoess-detail', {
            parent: 'entity',
            url: '/user-liked-videovoess/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'voessApp.userLikedVideo.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/user-liked-video/user-liked-videovoess-detail.html',
                    controller: 'UserLikedVideoVoessDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('userLikedVideo');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'UserLikedVideo', function($stateParams, UserLikedVideo) {
                    return UserLikedVideo.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'user-liked-videovoess',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('user-liked-videovoess-detail.edit', {
            parent: 'user-liked-videovoess-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/user-liked-video/user-liked-videovoess-dialog.html',
                    controller: 'UserLikedVideoVoessDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['UserLikedVideo', function(UserLikedVideo) {
                            return UserLikedVideo.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('user-liked-videovoess.new', {
            parent: 'user-liked-videovoess',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/user-liked-video/user-liked-videovoess-dialog.html',
                    controller: 'UserLikedVideoVoessDialogController',
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
                    $state.go('user-liked-videovoess', null, { reload: 'user-liked-videovoess' });
                }, function() {
                    $state.go('user-liked-videovoess');
                });
            }]
        })
        .state('user-liked-videovoess.edit', {
            parent: 'user-liked-videovoess',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/user-liked-video/user-liked-videovoess-dialog.html',
                    controller: 'UserLikedVideoVoessDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['UserLikedVideo', function(UserLikedVideo) {
                            return UserLikedVideo.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('user-liked-videovoess', null, { reload: 'user-liked-videovoess' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('user-liked-videovoess.delete', {
            parent: 'user-liked-videovoess',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/user-liked-video/user-liked-videovoess-delete-dialog.html',
                    controller: 'UserLikedVideoVoessDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['UserLikedVideo', function(UserLikedVideo) {
                            return UserLikedVideo.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('user-liked-videovoess', null, { reload: 'user-liked-videovoess' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
