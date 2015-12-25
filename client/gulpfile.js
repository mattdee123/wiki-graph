var gulp = require('gulp');
var ts = require('gulp-typescript');

gulp.task('templates', function() {
  return gulp.src('src/**/*.tpl.html')
    .pipe(gulp.dest('../src/main/resources/static/views'));
});

gulp.task('typescript', function () {
  var outDir = '../src/main/resources/static/js';
  return gulp.src('src/**/*.ts')
    .pipe(ts({
      noImplicitAny: false,
      experimentalDecorators: true,
      emitDecoratorMetadata: true,
      module: 'system',
      moduleResolution: 'node',
      target: 'ES5',
      outDir: outDir
    }))
    .pipe(gulp.dest(outDir));
});

gulp.task('default', ['typescript', 'templates'], function () {
  gulp.watch('src/**/*.ts', ['typescript']);
  gulp.watch('src/**/*.html', ['templates']);
});
