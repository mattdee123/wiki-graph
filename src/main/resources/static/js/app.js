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
    if (newRoute && newRoute.$$route) {
      $rootScope.currentController = newRoute.$$route.controller;
    }
  });
});

WG.service('Fetch', function($http) {
  var Fetch = {};

  Fetch.getGraph = function(form) {
    return $http({
      url: '/graph',
      method: 'GET',
      params: form
    });
  };

  Fetch.getLinks = function(form) {
    return $http({
      url: '/links',
      method: 'GET',
      params: form
    })
  };

  Fetch.getShortestPath = function(start, end) {
    return $http({
      url: '/path',
      method: 'GET',
      params: {
        start: start,
        end: end
      }
    });
  };

  return Fetch;
});

WG.service('Graph', function() {
  var Graph = {};

  Graph.refresh = function(data, onBeforeCompute) {
    $('#graph-canvaswidget').remove();

    if (!data) {
      return;
    }

    var rGraph = new $jit.RGraph({
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
          rGraph.onClick(node.id, {
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
      },
      onBeforeCompute: onBeforeCompute
    });

    rGraph.loadJSON(data);
    rGraph.graph.eachNode(function(n) {
      n.getPos().setc(-200, -200);
    });
    rGraph.compute('end');
    rGraph.refresh();
    rGraph.canvas.scale(2, 2);
  };

  return Graph;
});

WG.directive('tooltip', function() {
  var dir = {};
  dir.restrict = 'A';
  dir.link = function(scope, elem) {
    elem.tooltip({container: 'body', placement: 'right'});
  };
  return dir;
});
