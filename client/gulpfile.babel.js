import gulp from 'gulp';
import ts from 'gulp-typescript';
import scss from 'gulp-sass';
import autoprefixer from 'gulp-autoprefixer';
import cssnano from 'gulp-cssnano';

let tsProject = ts.createProject('tsconfig.json');

gulp.task('styles', () => {
  return gulp.src('scss/style.scss')
    .pipe(scss())
    .pipe(cssnano())
    .pipe(autoprefixer({
      browsers: ['last 2 versions'],
      cascade: false
    }))
    .pipe(gulp.dest('../src/main/resources/static/css'));
});

gulp.task('templates', () => {
  return gulp.src('src/**/*.tpl.html')
    .pipe(gulp.dest('../src/main/resources/static/views'));
});

gulp.task('typescript',  () => {
  return gulp.src('src/**/*.ts')
    .pipe(ts(tsProject))
    .pipe(gulp.dest('../src/main/resources/static/js'));
});

gulp.task('default', ['styles', 'typescript', 'templates'],  () => {
  gulp.watch('scss/**/*.scss', ['styles']);
  gulp.watch('src/**/*.ts', ['typescript']);
  gulp.watch('src/**/*.html', ['templates']);
});
