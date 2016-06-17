///<reference path="../typings/index.d.ts" />

import {bootstrap} from '@angular/platform-browser-dynamic';
import {provide} from '@angular/core';
import {LocationStrategy, HashLocationStrategy} from '@angular/common';
import {ROUTER_PROVIDERS, RouteConfig} from '@angular/router-deprecated';
import {HTTP_PROVIDERS} from "@angular/http";
import {QueryService} from "./query.service";
import {BaseComponent} from './base.component';

bootstrap(<any>BaseComponent, [
  HTTP_PROVIDERS,
  ROUTER_PROVIDERS,
  provide(LocationStrategy, {useClass: HashLocationStrategy}),
  QueryService
]);
