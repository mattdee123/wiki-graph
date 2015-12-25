import {Component} from 'angular2/core';
import {RouteConfig, ROUTER_DIRECTIVES} from 'angular2/router';

import {AlgosComponent} from './algos/algos.component';

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
])
export class BaseComponent {
}
