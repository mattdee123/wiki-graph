var WG = angular.module('WG', ['ngRoute']);

WG.filter('urlencode', function() {
  return encodeURIComponent;
});

WG.config(function($routeProvider) {
  $routeProvider
  .when('/graph', {
    templateUrl: '/js/views/graph.html',
    controller: 'GraphController',
    reloadOnSearch: false
  })
  .when('/algos', {
    templateUrl: '/js/views/algos.html',
    controller: 'AlgosController'
  })
  .when('/links', {
    templateUrl: '/js/views/links.html',
    controller: 'LinksController'
  })
  .otherwise({
    redirectTo: '/algos'
  });
});

WG.run(function($rootScope) {
  $rootScope.$on('$routeChangeSuccess', function(event, newRoute) {
    $rootScope.currentController = newRoute.$$route.controller;
  });
});

WG.service('Fetch', function($http) {
  var Fetch = {};

  Fetch.getGraph = function(form, successCallback, errorCallback) {
    successCallback = successCallback || function() {};
    errorCallback = errorCallback || function() {};
    var result = $http({
      url: '/graph',
      method: 'GET',
      params: form
    })
    .success(successCallback)
    .error(errorCallback);
    return result;
  };

  Fetch.getLinks = function(form, successCallback, errorCallback) {
    successCallback = successCallback || function() {};
    errorCallback = errorCallback || function() {};
    var result = $http({
      url: '/links',
      method: 'GET',
      params: form
    })
    .success(successCallback)
    .error(errorCallback);
    return result;
  };

  Fetch.getShortestPath = function(start, end, successCallback, errorCallback) {
    successCallback = successCallback || function() {};
    errorCallback = errorCallback || function() {};
    var result = $http({
      url: '/path',
      method: 'GET',
      params: {
        start: start,
        end: end
      }
    })
    .success(successCallback)
    .error(errorCallback);
    return result;
  };

  return Fetch;
});

WG.service('Graph', function() {
  var Graph = {};

  Graph.refresh = function(data) {
    $('#graph-canvaswidget').remove();

    if (!data) {
      return;
    }

    var rgraph = new $jit.RGraph({
      injectInto: 'graph',

      background: {
        numberOfCircles: 100,
        CanvasStyles: {
          strokeStyle: '#555'
        }
      },

      Navigation: {
        enable: true,
        zooming: 10,
        panning: true
      },

      Node: {
        color: '#0099dd',
        dim: 3
      },

      Tips: {
        enable: true,
        type: 'Native',
        offsetX: 10,
        offsetY: 10,
        onShow: function(tip, node) {
          tip.innerHTML = node.name;
        }
      },

      Edge: {
        color: '#0099dd',
        lineWidth: 0.5
      },

      Events: {
        enable: true,
        type: 'Native',
        onClick: function(node, eventInfo, e) {
          if (!node || node.nodeFrom) {
            return;
          }
          rgraph.onClick(node.id, {
            duration: 400,
            transition: $jit.Trans.Quad.easeInOut
          });
        }
      },

      onPlaceLabel: function(domElement, node) {
        var style = domElement.style;
        style.display = '';
        style.cursor = 'pointer';

        if (node._depth === 1) {
          style.fontSize = "0.8em";
          style.color = "#ccc";
        } else {
          style.display = 'none';
        }

        var left = parseInt(style.left, 10);
        var w = domElement.offsetWidth;
        style.left = (left - w / 2) + 'px';
      }
    });

    rgraph.loadJSON(data);
    rgraph.graph.eachNode(function (n) {
      n.getPos().setc(-200, -200);
    });
    rgraph.compute('end');
    rgraph.refresh();
    rgraph.canvas.scale(2, 2);
  };

  return Graph;
});

WG.directive('tooltip', function() {
  var dir = {};
  dir.restrict = 'A';
  dir.link = function(scope, elem) {
    $(elem).tooltip({container: 'body', placement: 'right'});
  };
  return dir;
});
