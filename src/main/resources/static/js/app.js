var WG = angular.module('WG', []);

WG.filter('urlencode', function() {
  return encodeURIComponent;
});

WG.config(function($routeProvider) {
  $routeProvider
  .when('/', {templateUrl: '/js/views/app.html', controller: 'BaseController'})
  .when('/:page/', {templateUrl: '/js/views/app.html', controller: 'BaseController'});
});

WG.factory('Data', function($http) {
  var Data = {};
  Data.loading = false;
  Data.links = [];
  Data.failure = false;
  return Data;
});

WG.service('Fetch', function($http) {
  var Fetch = {};
  Fetch.getLinks = function(page, successCallback, errorCallback) {
    successCallback = successCallback || function() {};
    errorCallback = errorCallback || function() {};
    var result = $http.get('/page?page='+page)
    .success(successCallback)
    .error(errorCallback);
    return result;
  };
  return Fetch;
});

WG.service('Graph', function() {
  var Graph = {};

  var formatData = function(data, maxChildren) {
    if (maxChildren === undefined) {
      maxChildren = 100;
    }

    var result = {
      id: '0',
      name: data.basePage
    };

    result.children = _.map(data.links, function(x, i) {
      return {
        id: 'level1' + i,
        name: x
      };
    });

    result.children = _.first(result.children, maxChildren);

    return result;
  };

  Graph.refresh = function(data) {
    var rgraph = new $jit.RGraph({
      injectInto: 'graph',

      background: {
        CanvasStyles: {
          strokeStyle: '#555'
        }
      },

      Navigation: {
        enable: true,
        zooming: 10
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
    $(elem).tooltip({container: 'body', position: 'bottom'});
  };
  return dir;
});
