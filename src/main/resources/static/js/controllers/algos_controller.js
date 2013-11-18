WG.controller('AlgosController', function($scope, Fetch) {
  $scope.form = {};

  $scope.shortestPath = function() {
    Fetch.getShortestPath($scope.shortestPath.start, $scope.shortestPath.end,
      function(result) {
        $scope.shortestPathResult = result;
      },
      function(error) {
        console.log(error);
      }
    );
  };
});
