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

  var refreshGraph = function(result) {
    $scope.data.loading = false;
    $scope.data.graph = result.data;
    $scope.data.error = false;
    Graph.refresh($scope.data.graph, function(node) {
      $scope.form.page = node.name;
      $scope.refresh();
    });
  };

  var refreshGraphError = function() {
    $scope.data.loading = false;
    $scope.data.graph = {};
    $scope.data.error = true;
  };

  var getGraph = function(form) {
    $scope.data.loading = true;
    $scope.data.basePage = form.page;
    $scope.data.error = false;
    Fetch.getGraph(form).then(refreshGraph, refreshGraphError);
  };

  if ($scope.form.page) {
    getGraph($scope.form);
  }

  $scope.refresh = function() {
    if (!($scope.form && $scope.form.page)) {
      return;
    }
    $location.search('page', $scope.form.page);
    $location.search('maxDepth', $scope.form.maxDepth);
    $location.search('maxDegree', $scope.form.maxDegree);
    $location.search('maxArticles', $scope.form.maxArticles);
    getGraph($scope.form);
  };
});
