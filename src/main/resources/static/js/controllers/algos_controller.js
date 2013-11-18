WG.controller('AlgosController', function($scope, Fetch) {
  $scope.form = {};
  $scope.shortestPathLoading = false;
  $scope.shortestPath = {};

  $scope.shortestPath = function() {
    $scope.shortestPathLoading = true;
    Fetch.getShortestPath($scope.shortestPath.start, $scope.shortestPath.end,
      function(result) {
        $scope.shortestPathLoading = false;
        $scope.shortestPathResult = result;
      },
      function(error) {
        $scope.shortestPathLoading = false;
        $scope.shortestPathResult = [];
      }
    );
  };

  $scope.switchShortestPath = function() {
    var tmp = $scope.shortestPath.start;
    $scope.shortestPath.start = $scope.shortestPath.end;
    $scope.shortestPath.end = tmp;
  };
});
