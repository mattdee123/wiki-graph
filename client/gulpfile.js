var gulp = require('gulp');
var ts = require('gulp-typescript');
var less = require('gulp-less');
var autoprefixer = require('gulp-autoprefixer');
var cssnano = require('gulp-cssnano');

gulp.task('styles', function() {
  return gulp.src('less/style.less')
    .pipe(less())
    .pipe(cssnano())
    .pipe(autoprefixer({
      browsers: ['last 2 versions'],
      cascade: false
    }))
    .pipe(gulp.dest('../src/main/resources/static/css'));
});

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

gulp.task('default', ['styles', 'typescript', 'templates'], function () {
  gulp.watch('less/**/*.less', ['styles']);
  gulp.watch('src/**/*.ts', ['typescript']);
  gulp.watch('src/**/*.html', ['templates']);
});
