(function() {
    'use strict';

    angular
        .module('voessApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('user-extvoess', {
            parent: 'entity',
            url: '/user-extvoess',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'voessApp.userExt.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/user-ext/user-extsvoess.html',
                    controller: 'UserExtVoessController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('userExt');
                    $translatePartialLoader.addPart('sexGender');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('user-extvoess-detail', {
            parent: 'entity',
            url: '/user-extvoess/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'voessApp.userExt.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/user-ext/user-extvoess-detail.html',
                    controller: 'UserExtVoessDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('userExt');
                    $translatePartialLoader.addPart('sexGender');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'UserExt', function($stateParams, UserExt) {
                    return UserExt.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'user-extvoess',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('user-extvoess-detail.edit', {
            parent: 'user-extvoess-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/user-ext/user-extvoess-dialog.html',
                    controller: 'UserExtVoessDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['UserExt', function(UserExt) {
                            return UserExt.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('user-extvoess.new', {
            parent: 'user-extvoess',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/user-ext/user-extvoess-dialog.html',
                    controller: 'UserExtVoessDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                userAge: null,
                                userSex: null,
                                userImage: null,
                                userImageContentType: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('user-extvoess', null, { reload: 'user-extvoess' });
                }, function() {
                    $state.go('user-extvoess');
                });
            }]
        })
        .state('user-extvoess.edit', {
            parent: 'user-extvoess',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/user-ext/user-extvoess-dialog.html',
                    controller: 'UserExtVoessDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['UserExt', function(UserExt) {
                            return UserExt.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('user-extvoess', null, { reload: 'user-extvoess' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('user-extvoess.delete', {
            parent: 'user-extvoess',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/user-ext/user-extvoess-delete-dialog.html',
                    controller: 'UserExtVoessDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['UserExt', function(UserExt) {
                            return UserExt.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('user-extvoess', null, { reload: 'user-extvoess' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
