<!DOCTYPE html>
<html>
<head>
  <title>Wiki Graph</title>

  <link rel="stylesheet" href="/css/bootstrap.min.css">
  <link href="//netdna.bootstrapcdn.com/font-awesome/3.2.1/css/font-awesome.css" rel="stylesheet">

  <link rel="stylesheet" href="/css/index.css">
</head>
<body ng-app="WG">
  <div class="nav-links">
    <a href="#/" class="btn btn-primary" tooltip title="Visualization">
      <i class="icon-code-fork"></i>
    </a>
    <br> <br>
    <a href="#/algos" class="btn btn-primary" tooltip title="Analysis">
      <i class="icon-bolt"></i>
    </a>
    <br> <br>
    <a href="#/links" class="btn btn-primary" tooltip title="Links">
      <i class="icon-list"></i>
    </a>
  </div>
  <ng-view></ng-view>
  <script src="//ajax.googleapis.com/ajax/libs/jquery/1.10.1/jquery.min.js"></script>
  <script src="//ajax.googleapis.com/ajax/libs/angularjs/1.1.5/angular.min.js"></script>
  <script src="//cdnjs.cloudflare.com/ajax/libs/d3/3.3.3/d3.min.js"></script>
  <script src="/js/lib/underscore.min.js"></script>
  <script src="/js/lib/bootstrap.min.js"></script>
  <script src="/js/lib/jit.min.js"></script>

  <script src="/js/app.js"></script>
  <script src="/js/controllers/base_controller.js"></script>
  <script src="/js/controllers/algos_controller.js"></script>
  <script src="/js/controllers/links_controller.js"></script>
</body>
</html>
