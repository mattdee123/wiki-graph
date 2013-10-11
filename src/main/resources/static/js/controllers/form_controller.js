WG.controller('FormController', function FormController($scope, $routeParams, $location, Data) {
  $scope.data = Data;
  $scope.form = $scope.form || {};

  $scope.refresh = function() {
    if (!($scope.form && $scope.form.page)) {
      return;
    }
    $location.path('/' + $scope.form.page);
  };

  console.log($routeParams);
  if ($routeParams.page) {
    $scope.form.page = $routeParams.page;

    $scope.data.loading = true;
    $scope.data.basePage = $scope.form.page;
    $scope.data.failure = false;
    Data.getLinks($scope.form.page,
      function(result) {
        $scope.data.loading = false;
        $scope.data.links = result;
      },
      function(result) {
        $scope.data.loading = false;
        $scope.data.links = [];
        $scope.data.failure = true;
      }
    );
  } else {
    $scope.form.page = '';
    $scope.data.links = [];
    $scope.data.loading = false;
    $scope.data.failure = false;
  }
});
