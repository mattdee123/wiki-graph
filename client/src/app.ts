import {bootstrap} from 'angular2/platform/browser';
import {provide} from 'angular2/core';
import {ROUTER_PROVIDERS} from 'angular2/router';
import {LocationStrategy, HashLocationStrategy} from 'angular2/router';
import {BaseComponent} from './base.component';
import {HTTP_PROVIDERS} from "angular2/http";
import {QueryService} from "./query.service";

bootstrap(BaseComponent, [
  HTTP_PROVIDERS,
  ROUTER_PROVIDERS,
  provide(LocationStrategy, {useClass: HashLocationStrategy}),
  QueryService
]);
