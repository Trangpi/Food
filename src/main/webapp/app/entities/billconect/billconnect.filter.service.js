(function() {
    'use strict';

    angular
        .module('foodApp')
        .factory('BillconectFilter', BillconectFilter);

    BillconectFilter.$inject = ['$resource'];

    function BillconectFilter($resource) {
        var resourceUrl =  'api/danhsachMAdh';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
