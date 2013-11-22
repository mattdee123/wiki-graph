/**
 * Algorithms controller. Used on the #/algos page.
 */

WG.controller('AlgosController', function ($scope, $location, $routeParams, $http, Fetch) {
  $scope.shortestPath = {
    loading: false,
    error: null,
    start: $routeParams.shortestPathStart,
    end: $routeParams.shortestPathEnd
  };

  // Fetches the data required for the shortest path algorithm.
  $scope.shortestPath.fetch = function() {
    $scope.shortestPath.loading = true;
    $scope.shortestPath.error = null;
    Fetch.getShortestPath($scope.shortestPath.start, $scope.shortestPath.end,
      function(result) {
        $scope.shortestPath.loading = false;
        $scope.shortestPath.error = null;
        $scope.shortestPath.result = result;
      },
      function(error) {
        $scope.shortestPath.loading = false;
        $scope.shortestPath.error = error;
        $scope.shortestPath.result = [];
      }
    );
  };

  if ($scope.shortestPath.start && $scope.shortestPath.end) {
    $scope.shortestPath.fetch();
  }

  // Submit handler for the submission of shortestPath.
  $scope.shortestPath.submit = function() {
    $location.search('shortestPathStart', $scope.shortestPath.start);
    $location.search('shortestPathEnd', $scope.shortestPath.end);
    $scope.shortestPath.fetch();
  };

  // Swaps the values of the shortestPath start and end
  $scope.shortestPath.swap = function() {
    var tmp = $scope.shortestPath.start;
    $scope.shortestPath.start = $scope.shortestPath.end;
    $scope.shortestPath.end = tmp;
  };

  // Randomly selects a start and end article.
  $scope.shortestPath.randomize = function() {
    $http({
      method: 'GET',
      url: '/api/randomArticle?count=2', // Prevents Angular caching the random article.
    })
    .success(function(articles) {
      $scope.shortestPath.start = articles[0];
      $scope.shortestPath.end = articles[1];
    });
  };
});
