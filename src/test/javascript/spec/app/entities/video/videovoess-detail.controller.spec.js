'use strict';

describe('Controller Tests', function() {

    describe('Video Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockVideo, MockGame, MockTeam, MockTournament, MockPlayer;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockVideo = jasmine.createSpy('MockVideo');
            MockGame = jasmine.createSpy('MockGame');
            MockTeam = jasmine.createSpy('MockTeam');
            MockTournament = jasmine.createSpy('MockTournament');
            MockPlayer = jasmine.createSpy('MockPlayer');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Video': MockVideo,
                'Game': MockGame,
                'Team': MockTeam,
                'Tournament': MockTournament,
                'Player': MockPlayer
            };
            createController = function() {
                $injector.get('$controller')("VideoVoessDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'voessApp:videoUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
