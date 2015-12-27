import {Component} from 'angular2/core';
import {RouteConfig, ROUTER_DIRECTIVES} from 'angular2/router';

import {AlgosComponent} from './algos/algos.component';
import {LinksComponent} from "./links/links.component";
import {GraphComponent} from "./graph/graph.component";

@Component({
  selector: 'wg-app',
  templateUrl: 'views/base.tpl.html',
  directives: [ROUTER_DIRECTIVES]
})
@RouteConfig([
  {
    path: '/algos',
    name: 'Algos',
    component: AlgosComponent,
    useAsDefault: true
  },
  {
    path: '/graph',
    name: 'Graph',
    component: GraphComponent
  },
  {
    path: '/links',
    name: 'Links',
    component: LinksComponent
  }
])
export class BaseComponent {
}
