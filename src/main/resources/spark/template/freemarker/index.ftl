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

  <script src="https://code.angularjs.org/tools/system.js"></script>
  <script src="https://code.angularjs.org/2.0.0-beta.0/angular2-polyfills.js"></script>
  <script src="https://code.angularjs.org/2.0.0-beta.0/Rx.js"></script>
  <script src="https://code.angularjs.org/2.0.0-beta.0/angular2.dev.js"></script>
  <script src="https://code.angularjs.org/2.0.0-beta.0/http.dev.js"></script>
  <script src="https://code.angularjs.org/2.0.0-beta.0/router.dev.js"></script>

  <script>
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

  <script src="/js/lib/underscore.min.js"></script>
  <script src="/js/lib/jit.min.js"></script>
</body>
</html>
