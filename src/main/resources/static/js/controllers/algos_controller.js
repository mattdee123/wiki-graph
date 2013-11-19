WG.controller('AlgosController', function ($scope, $location, $routeParams, Fetch) {
  $scope.shortestPathLoading = false;
  $scope.shortestPathError = null;
  $scope.form = {};
  $scope.form.shortestPath = {
    start: 'hi', end: 'hihihi'
  };

  $scope.form.shortestPath.start = $routeParams.shortestPathStart;
  $scope.form.shortestPath.end = $routeParams.shortestPathEnd;

  if ($scope.form.shortestPath.start && $scope.form.shortestPath.end) {
    $scope.shortestPathLoading = true;
    $scope.shortestPathError = null;
    Fetch.getShortestPath($scope.form.shortestPath.start, $scope.form.shortestPath.end,
      function(result) {
        $scope.shortestPathLoading = false;
        $scope.shortestPathError = null;
        $scope.shortestPathResult = result;
      },
      function(error) {
        $scope.shortestPathLoading = false;
        $scope.shortestPathError = error;
        $scope.shortestPathResult = [];
      }
    );
  }

  $scope.shortestPath = function() {
    $location.search('shortestPathStart', $scope.form.shortestPath.start);
    $location.search('shortestPathEnd', $scope.form.shortestPath.end);
    $scope.shortestPathLoading = true;
    $scope.shortestPathError = null;
    Fetch.getShortestPath($scope.form.shortestPath.start, $scope.form.shortestPath.end,
      function(result) {
        $scope.shortestPathLoading = false;
        $scope.shortestPathError = null;
        $scope.shortestPathResult = result;
      },
      function(error) {
        $scope.shortestPathLoading = false;
        $scope.shortestPathError = error;
        $scope.shortestPathResult = [];
      }
    );
  };

  $scope.switchShortestPath = function() {
    var tmp = $scope.form.shortestPath.start;
    $scope.form.shortestPath.start = $scope.form.shortestPath.end;
    $scope.form.shortestPath.end = tmp;
  };
});
