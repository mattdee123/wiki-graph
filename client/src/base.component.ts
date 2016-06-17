import {Component} from '@angular/core';
import {ROUTER_DIRECTIVES, RouteConfig} from '@angular/router-deprecated';

import {AlgosComponent} from './algos/algos.component';
import {LinksComponent} from "./links/links.component";
import {GraphComponent} from "./graph/graph.component";

@Component({
  selector: 'wg-app',
  templateUrl: 'views/base.tpl.html',
  directives: [ROUTER_DIRECTIVES, <any>AlgosComponent]
})
@RouteConfig([
  {
    path: '/algos',
    component: <any>AlgosComponent,
    useAsDefault: true
  },
  {
    path: '/graph',
    component: <any>GraphComponent
  },
  {
    path: '/links',
    component: <any>LinksComponent
  }
])
export class BaseComponent {
  constructor() {}
}
