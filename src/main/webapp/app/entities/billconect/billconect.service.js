(function() {
    'use strict';
    angular
        .module('foodApp')
        .factory('Billconect', Billconect);

    Billconect.$inject = ['$resource'];

    function Billconect ($resource) {
        var resourceUrl =  'api/billconects/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
