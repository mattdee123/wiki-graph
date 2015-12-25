import {Component} from 'angular2/core';
import {Inject} from "angular2/core";
import {ROUTER_DIRECTIVES} from 'angular2/router';
import {CORE_DIRECTIVES} from "angular2/common";

import {QueryService} from '../query.service';
import {ShortestPathComponent} from './shortestpath.component';

@Component({
  templateUrl: 'views/algos/algos.tpl.html',
  directives: [ROUTER_DIRECTIVES, CORE_DIRECTIVES, ShortestPathComponent]
})
export class AlgosComponent {
}
