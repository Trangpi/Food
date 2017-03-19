(function() {
    'use strict';

    angular
        .module('foodApp')
        .factory('MonAnSearch', MonAnSearch);

    MonAnSearch.$inject = ['$resource'];

    function MonAnSearch($resource) {
        var resourceUrl =  'api/_search/mon-ans/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
