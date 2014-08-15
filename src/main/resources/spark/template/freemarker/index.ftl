<!DOCTYPE html>
<html>
<head>
  <title>Wiki Graph</title>

  <link href="//maxcdn.bootstrapcdn.com/bootswatch/3.2.0/cyborg/bootstrap.min.css" rel="stylesheet">
  <link href="//netdna.bootstrapcdn.com/font-awesome/4.1.0/css/font-awesome.min.css" rel="stylesheet">

  <link rel="stylesheet" href="/css/index.css">
</head>
<body ng-app="WG">
  <a href="https://github.com/mattdee123/wiki-graph">
    <img style="position: absolute; top: 0; right: 0; border: 0; z-index: 1"
    src="//camo.githubusercontent.com/a6677b08c955af8400f44c6298f40e7d19cc5b2d/68747470733a2f2f73332e616d617a6f6e6177732e636f6d2f6769746875622f726962626f6e732f666f726b6d655f72696768745f677261795f3664366436642e706e67"
    alt="Fork me on GitHub"
    data-canonical-src="//s3.amazonaws.com/github/ribbons/forkme_right_gray_6d6d6d.png">
  </a>

  <nav class="navbar navbar-default" role="navigation">
    <div class="container-fluid">
      <div class="navbar-header">
        <button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#bs-navbar-collapse-1">
          <span class="sr-only">Toggle navigation</span>
          <span class="icon-bar"></span>
          <span class="icon-bar"></span>
          <span class="icon-bar"></span>
        </button>
      </div>

      <div class="collapse navbar-collapse" id="bs-navbar-collapse-1">
        <ul class="nav navbar-nav">
          <li ng-class="{active: currentController === 'GraphController'}"><a href="#/graph">Visualize</a></li>
          <li ng-class="{active: currentController === 'AlgosController'}"><a href="#/algos">Analyze</a></li>
          <li ng-class="{active: currentController === 'LinksController'}"><a href="#/links">Links</a></li>
        </ul>
      </div>
    </div>
  </nav>
  <div class="container">
    <ng-view></ng-view>
  </div>
  <script src="//ajax.googleapis.com/ajax/libs/jquery/1.10.1/jquery.min.js"></script>
  <script src="//ajax.googleapis.com/ajax/libs/angularjs/1.2.19/angular.min.js"></script>
  <script src="//cdnjs.cloudflare.com/ajax/libs/angular.js/1.2.20/angular-route.min.js"></script>
  <script src="//cdnjs.cloudflare.com/ajax/libs/d3/3.3.3/d3.min.js"></script>
  <script src="//maxcdn.bootstrapcdn.com/bootstrap/3.2.0/js/bootstrap.min.js"></script>
  <script src="/js/lib/underscore.min.js"></script>
  <script src="/js/lib/jit.min.js"></script>

  <script src="/js/app.js"></script>
  <script src="/js/controllers/graph_controller.js"></script>
  <script src="/js/controllers/algos_controller.js"></script>
  <script src="/js/controllers/links_controller.js"></script>
</body>
</html>
