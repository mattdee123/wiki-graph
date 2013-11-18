WG.controller('BaseController', function BaseController($scope, $routeParams, $location, Data, Fetch, Graph) {
  $scope.data = Data;
  $scope.form = $scope.form || {};

  $scope.form.maxDepth = parseInt($routeParams.maxDepth, 10) || 5;
  $scope.form.maxDegree = parseInt($routeParams.maxDegree, 10) || 20;
  $scope.form.maxArticles = parseInt($routeParams.maxArticles, 10) || 100;

  $scope.refresh = function() {
    if (!($scope.form && $scope.form.page)) {
      return;
    }
    $location.search('page', $scope.form.page);
    $location.search('maxDepth', $scope.form.maxDepth);
    $location.search('maxDegree', $scope.form.maxDegree);
    $location.search('maxArticles', $scope.form.maxArticles);

    $scope.data.loading = true;
    $scope.data.basePage = $scope.form.page;
    $scope.data.failure = false;

    Fetch.getLinks($scope.form,
      function(result) {
        $scope.data.loading = false;
        $scope.data.graph = result;
        $scope.data.failure = false;
        Graph.refresh($scope.data.graph);
      },
      function(result) {
        $scope.data.loading = false;
        $scope.data.graph = [];
        $scope.data.failure = true;
        Graph.refresh($scope.data.graph);
      }
    );
  };

  if ($routeParams.page) {
    $scope.form.page = $routeParams.page;
    $scope.data.loading = true;
    $scope.data.basePage = $scope.form.page;
    $scope.data.failure = false;

    Fetch.getLinks($scope.form,
      function(result) {
        $scope.data.loading = false;
        $scope.data.graph = result;
        $scope.data.failure = false;
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
