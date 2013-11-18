WG.controller('BaseController', function BaseController($scope, $routeParams, $location, Data, Fetch, Graph) {
  $scope.data = Data;
  $scope.form = $scope.form || {};

  $scope.refresh = function() {
    if (!($scope.form && $scope.form.page)) {
      return;
    }
    $location.path('/' + $scope.form.page + '/');
  };

  if ($routeParams.page) {
    $scope.form.page = $routeParams.page;
    $scope.data.loading = true;
    $scope.data.basePage = $scope.form.page;
    $scope.data.failure = false;

    Fetch.getLinks($scope.form.page,
      function(result) {
        $scope.data.loading = false;
        $scope.data.graph = result;
        Graph.refresh($scope.data.graph);
      },
      function(result) {
        $scope.data.loading = false;
        $scope.data.graph = [];
        $scope.data.failure = true;
        Graph.refresh($scope.data.graph);
      }
    );
  } else {
    $scope.form.page = '';
    $scope.data.graph = {};
    $scope.data.loading = false;
    $scope.data.failure = false;
  }
});
