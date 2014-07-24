WG.controller('GraphController', function($scope, $routeParams, $location, Fetch, Graph) {
  $scope.data = {
    loading: false,
    basePage: null,
    error: false,
    graph: {}
  };

  $scope.form = {
    page: $routeParams.page,
    maxDepth: parseInt($routeParams.maxDepth, 10) || 5,
    maxDegree: parseInt($routeParams.maxDegree, 10) || 20,
    maxArticles: parseInt($routeParams.maxArticles, 10) || 100
  };

  var getGraph = function() {
    $scope.data.loading = true;
    $scope.data.basePage = $scope.form.page;
    $scope.data.error = false;

    Fetch.getGraph($scope.form,
      function(result) {
        $scope.data.loading = false;
        $scope.data.graph = result;
        $scope.data.error = false;
        Graph.refresh($scope.data.graph);
      },
      function(result) {
        $scope.data.loading = false;
        $scope.data.graph = {};
        $scope.data.error = true;
      }
    );
  };

  if ($scope.form.page) {
    getGraph();
  }

  $scope.refresh = function() {
    if (!($scope.form && $scope.form.page)) {
      return;
    }
    $location.search('page', $scope.form.page);
    $location.search('maxDepth', $scope.form.maxDepth);
    $location.search('maxDegree', $scope.form.maxDegree);
    $location.search('maxArticles', $scope.form.maxArticles);
    getGraph();
  };
});
