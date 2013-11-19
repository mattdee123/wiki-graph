WG.controller('LinksController', function BaseController($scope, $routeParams, $location, Data, Fetch, Graph) {
  $scope.data = {};
  $scope.form = $scope.form || {};

  $scope.refresh = function() {
    if (!($scope.form && $scope.form.page)) {
      return;
    }
    $location.search('page', $scope.form.page);

    $scope.data.loading = true;
    $scope.data.basePage = $scope.form.page;
    $scope.data.error = false;

    Fetch.getLinks($scope.form,
      function(result) {
        $scope.data.loading = false;
        $scope.data.links = result;
        $scope.data.error = false;
      },
      function(result) {
        $scope.data.loading = false;
        $scope.data.links = {};
        $scope.data.error = result;
      }
    );
  };

  if ($routeParams.page) {
    $scope.form.page = $routeParams.page;
    $scope.data.loading = true;
    $scope.data.basePage = $scope.form.page;
    $scope.data.error = false;

    Fetch.getLinks($scope.form,
      function(result) {
        $scope.data.loading = false;
        $scope.data.links = result;
        $scope.data.error = false;
      },
      function(result) {
        $scope.data.loading = false;
        $scope.data.links = {};
        $scope.data.error = result;
      }
    );
  } else {
    $scope.form.page = '';
    $scope.data.links = {};
    $scope.data.loading = false;
    $scope.data.error = false;
  }
});
