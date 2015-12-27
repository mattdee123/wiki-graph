<!DOCTYPE html>
<html>
<head>
  <title>Wiki Graph</title>

  <link href="//maxcdn.bootstrapcdn.com/bootswatch/3.2.0/cyborg/bootstrap.min.css" rel="stylesheet">
  <link href="//netdna.bootstrapcdn.com/font-awesome/4.1.0/css/font-awesome.min.css" rel="stylesheet">

  <link rel="stylesheet" href="/css/style.css">

  <base href="/">
</head>
<body>
  <wg-app></wg-app>

  <script src="//code.jquery.com/jquery-1.11.3.min.js"></script>

  <script src="//code.angularjs.org/tools/system.js"></script>
  <script src="//code.angularjs.org/2.0.0-beta.0/angular2-polyfills.js"></script>
  <script src="//code.angularjs.org/2.0.0-beta.0/Rx.js"></script>

  <script src="//code.angularjs.org/2.0.0-beta.0/angular2.dev.js"></script>
  <script src="//code.angularjs.org/2.0.0-beta.0/http.dev.js"></script>
  <script src="//code.angularjs.org/2.0.0-beta.0/router.dev.js"></script>

  <script src="//cdnjs.cloudflare.com/ajax/libs/lodash.js/3.10.1/lodash.min.js"></script>
  <script src="//cdnjs.cloudflare.com/ajax/libs/jit/2.0.1/jit.min.js"></script>
  <script src="//cdnjs.cloudflare.com/ajax/libs/URI.js/1.17.0/URI.min.js"></script>

  <script>
    // load app.js
    System.config({
      packages: {
        js: {
          format: 'register',
          defaultExtension: 'js'
        }
      }
    });
    System.import('js/app')
      .then(null, console.error.bind(console));
  </script>

</body>
</html>
