<!DOCTYPE html>
<html>
<head>
  <title>Wiki Graph</title>

  <link href="//maxcdn.bootstrapcdn.com/bootswatch/3.2.0/cyborg/bootstrap.min.css" rel="stylesheet">
  <link href="//netdna.bootstrapcdn.com/font-awesome/4.1.0/css/font-awesome.min.css" rel="stylesheet">

  <link rel="stylesheet" href="/css/index.css">
</head>
<body ng-app="WG">
  <nav class="navbar navbar-default" role="navigation">
    <div class="container-fluid">
      <div class="navbar-header">
        <button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#bs-navbar-collapse-1">
          <span class="sr-only">Toggle navigation</span>
          <span class="icon-bar"></span>
          <span class="icon-bar"></span>
          <span class="icon-bar"></span>
        </button>
        <a class="navbar-brand" href="#/">Wiki Graph</a>
      </div>

      <div class="collapse navbar-collapse" id="bs-navbar-collapse-1">
        <ul class="nav navbar-nav">
          <li ng-class="{active: currentController === 'BaseController'}"><a href="#/">Visualize</a></li>
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
  <script src="/js/controllers/base_controller.js"></script>
  <script src="/js/controllers/algos_controller.js"></script>
  <script src="/js/controllers/links_controller.js"></script>
</body>
</html>
