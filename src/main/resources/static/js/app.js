var WG = angular.module('WG', []);

WG.filter('urlencode', function() {
  return encodeURIComponent;
});

WG.config(function($routeProvider) {
  $routeProvider
  .when('/:page', {templateUrl: '/js/views/app.html', controller: 'FormController'})
  .when('/', {templateUrl: '/js/views/app.html', controller: 'FormController'});
});

WG.factory('Data', function($http) {
  var Data = {};
  Data.loading = false;
  Data.links = [];
  Data.failure = false;

  Data.getLinks = function(page, successCallback, errorCallback) {
    successCallback = successCallback || function() {};
    errorCallback = errorCallback || function() {};
    var result = $http.get('/page?page='+page)
                      .success(successCallback)
                      .error(errorCallback);
    return result;
  };

  return Data;
});
