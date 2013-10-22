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

  Graph.refresh = function(data) {
    var graph = {};
    graph.nodes = [];
    graph.links = [];

    graph.nodes.push({
      name: data.basePage,
      group: 0
    });

    for (var i = 1; i < Math.min(data.links.length, 100); i++) {
      graph.nodes.push({
        name: data.links[i],
        group: 1
      });
      graph.links.push({
        source: 0,
        target: i,
        value: 1
      });
    }

    var width = 960;
    var height = 600;

    var color = d3.scale.category20();

    var force = d3.layout.force().charge(-1000).linkDistance(50).size([width, height]);

    var svg = d3.select("#graph").append("svg").attr("width", width).attr("height", height);

    force.nodes(graph.nodes).links(graph.links).start();

    var link = svg.selectAll(".link")
    .data(graph.links)
    .enter().append("line")
    .attr("class", "link")
    .style("stroke-width", function(d) { return d.value; });

    var node = svg.selectAll(".node")
    .data(graph.nodes)
    .enter().append("circle")
    .attr("class", "node")
    .attr("tooltip", '')
    .attr("title", function(d) { console.log(d.name); return d.name; })
    .attr("r", 5)
    .style("fill", function(d) { return color(d.group); })
    .call(force.drag);

    node.append("title")
    .text(function(d) { return d.name; });

    force.on("tick", function() {
      link.attr("x1", function(d) { return d.source.x; })
      .attr("y1", function(d) { return d.source.y; })
      .attr("x2", function(d) { return d.target.x; })
      .attr("y2", function(d) { return d.target.y; });

      node.attr("cx", function(d) { return d.x; })
      .attr("cy", function(d) { return d.y; });
    });

    d3.selectAll('.node').call(bootstrap.tooltip().placement('right'));
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
