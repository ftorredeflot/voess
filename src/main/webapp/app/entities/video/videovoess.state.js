(function() {
    'use strict';

    angular
        .module('voessApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('videovoess', {
            parent: 'entity',
            url: '/videovoess',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'voessApp.video.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/video/videosvoess.html',
                    controller: 'VideoVoessController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('video');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('videovoess-detail', {
            parent: 'entity',
            url: '/videovoess/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'voessApp.video.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/video/videovoess-detail.html',
                    controller: 'VideoVoessDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('video');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Video', function($stateParams, Video) {
                    return Video.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'videovoess',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('videovoess-detail.edit', {
            parent: 'videovoess-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/video/videovoess-dialog.html',
                    controller: 'VideoVoessDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Video', function(Video) {
                            return Video.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('videovoess.new', {
            parent: 'videovoess',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/video/videovoess-dialog.html',
                    controller: 'VideoVoessDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                videoName: null,
                                videoDuration: null,
                                videoDate: null,
                                videoViewerCount: null,
                                videoViewerCountLive: null,
                                videoUrl: null,
                                videoPicks: null,
                                videoGameStart: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('videovoess', null, { reload: 'videovoess' });
                }, function() {
                    $state.go('videovoess');
                });
            }]
        })
        .state('videovoess.edit', {
            parent: 'videovoess',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/video/videovoess-dialog.html',
                    controller: 'VideoVoessDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Video', function(Video) {
                            return Video.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('videovoess', null, { reload: 'videovoess' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('videovoess.delete', {
            parent: 'videovoess',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/video/videovoess-delete-dialog.html',
                    controller: 'VideoVoessDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Video', function(Video) {
                            return Video.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('videovoess', null, { reload: 'videovoess' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
