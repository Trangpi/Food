(function() {
    'use strict';
    angular
        .module('foodApp')
        .factory('MonAn', MonAn);

    MonAn.$inject = ['$resource', 'DateUtils'];


    function MonAn ($resource, DateUtils) {
        var resourceUrl =  'api/mon-ans/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.date_create = DateUtils.convertLocalDateFromServer(data.date_create);
                    }
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.date_create = DateUtils.convertLocalDateToServer(copy.date_create);
                    return angular.toJson(copy);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.date_create = DateUtils.convertLocalDateToServer(copy.date_create);
                    return angular.toJson(copy);
                }
            }
        });
    }
})();
