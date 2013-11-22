/**
 * Algorithms controller. Used on the #/algos page.
 */

WG.controller('AlgosController', function ($scope, $location, $routeParams, $http, Fetch) {
  $scope.data = {
    shortestPath: {
      loading: false,
      error: null,
      start: $routeParams.shortestPathStart,
      end: $routeParams.shortestPathEnd
    }
  };

  // Fetches the data required for the shortest path algorithm.
  $scope.data.shortestPath.fetch = function() {
    $scope.data.shortestPath.loading = true;
    $scope.data.shortestPath.error = null;
    Fetch.getShortestPath($scope.data.shortestPath.start, $scope.data.shortestPath.end,
      function(result) {
        $scope.data.shortestPath.loading = false;
        $scope.data.shortestPath.error = null;
        $scope.data.shortestPath.result = result;
      },
      function(error) {
        $scope.data.shortestPath.loading = false;
        $scope.data.shortestPath.error = error;
        $scope.data.shortestPath.result = [];
      }
    );
  };

  if ($scope.data.shortestPath.start && $scope.data.shortestPath.end) {
    $scope.data.shortestPath.fetch();
  }

  // Submit handler for the submission of shortestPath.
  $scope.data.shortestPath.submit = function() {
    $location.search('shortestPathStart', $scope.data.shortestPath.start);
    $location.search('shortestPathEnd', $scope.data.shortestPath.end);
    $scope.data.shortestPath.fetch();
  };

  // Swaps the values of the shortestPath start and end
  $scope.data.shortestPath.swap = function() {
    var tmp = $scope.data.shortestPath.start;
    $scope.data.shortestPath.start = $scope.data.shortestPath.end;
    $scope.data.shortestPath.end = tmp;
  };

  // Randomly selects a start and end article.
  $scope.data.shortestPath.randomize = function() {
    $http({
      method: 'GET',
      url: '/api/randomArticle?start=1', // Prevents Angular caching the random article.
    })
    .success(function(article) {
      $scope.data.shortestPath.start = article;
    });

    $http({
      method: 'GET',
      url: '/api/randomArticle?end=1',
    })
    .success(function(article) {
      $scope.data.shortestPath.end = article;
    });
  };
});
