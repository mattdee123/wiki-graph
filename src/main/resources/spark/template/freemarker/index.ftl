<!DOCTYPE html>
<html>
<head>
  <title>Wiki Graph</title>

  <link rel="stylesheet" href="/css/bootstrap.min.css">
  <link href="//netdna.bootstrapcdn.com/font-awesome/3.2.1/css/font-awesome.css" rel="stylesheet">

  <link rel="stylesheet" href="/css/index.css">
</head>
<body>
  <div class="container">
    <div class="jumbotron">
      <h1>Wiki Graph</h1>
      <div class="input-group">
        <input id="input-page" class="form-control input-large" type="text" placeholder="Page to query">
        <span class="input-group-btn">
          <button id="btn-refresh" class="btn btn-primary btn-large">Refresh</button>
        </span>
      </div>
    </div>

    <div class="panel" id="panel-links">
      <div class="panel-heading">
        <h2>Links on page - <span id="count-links">0</span> links found</h2>
      </div>
      <div id="links">
        <h3>Run a query to see the links.</h3>
      </div>
    </div>
  </div>
  <script src="//ajax.googleapis.com/ajax/libs/jquery/1.10.1/jquery.min.js"></script>
  <script src="/js/bootstrap.min.js"></script>
  <script src="/js/index.js"></script>
</body>
</html>
