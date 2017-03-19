'use strict';

describe('Controller Tests', function() {

    describe('Billconect Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockBillconect, MockMonAn, MockBill;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockBillconect = jasmine.createSpy('MockBillconect');
            MockMonAn = jasmine.createSpy('MockMonAn');
            MockBill = jasmine.createSpy('MockBill');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Billconect': MockBillconect,
                'MonAn': MockMonAn,
                'Bill': MockBill
            };
            createController = function() {
                $injector.get('$controller')("BillconectDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'foodApp:billconectUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
