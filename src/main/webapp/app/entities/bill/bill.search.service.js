(function() {
    'use strict';

    angular
        .module('foodApp')
        .factory('BillSearch', BillSearch);

    BillSearch.$inject = ['$resource'];

    function BillSearch($resource) {
        var resourceUrl =  'api/_search/bills/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
