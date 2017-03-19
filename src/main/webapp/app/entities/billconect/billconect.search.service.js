(function() {
    'use strict';

    angular
        .module('foodApp')
        .factory('BillconectSearch', BillconectSearch);

    BillconectSearch.$inject = ['$resource'];

    function BillconectSearch($resource) {
        var resourceUrl =  'api/_search/billconects/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
