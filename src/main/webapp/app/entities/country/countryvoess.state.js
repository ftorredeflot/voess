(function() {
    'use strict';

    angular
        .module('voessApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('countryvoess', {
            parent: 'entity',
            url: '/countryvoess',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'voessApp.country.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/country/countriesvoess.html',
                    controller: 'CountryVoessController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('country');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('countryvoess-detail', {
            parent: 'entity',
            url: '/countryvoess/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'voessApp.country.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/country/countryvoess-detail.html',
                    controller: 'CountryVoessDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('country');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Country', function($stateParams, Country) {
                    return Country.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'countryvoess',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('countryvoess-detail.edit', {
            parent: 'countryvoess-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/country/countryvoess-dialog.html',
                    controller: 'CountryVoessDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Country', function(Country) {
                            return Country.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('countryvoess.new', {
            parent: 'countryvoess',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/country/countryvoess-dialog.html',
                    controller: 'CountryVoessDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                countryName: null,
                                coutryFlag: null,
                                coutryFlagContentType: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('countryvoess', null, { reload: 'countryvoess' });
                }, function() {
                    $state.go('countryvoess');
                });
            }]
        })
        .state('countryvoess.edit', {
            parent: 'countryvoess',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/country/countryvoess-dialog.html',
                    controller: 'CountryVoessDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Country', function(Country) {
                            return Country.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('countryvoess', null, { reload: 'countryvoess' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('countryvoess.delete', {
            parent: 'countryvoess',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/country/countryvoess-delete-dialog.html',
                    controller: 'CountryVoessDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Country', function(Country) {
                            return Country.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('countryvoess', null, { reload: 'countryvoess' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
