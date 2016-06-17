import {Component} from '@angular/core';
import {Inject} from "@angular/core";
import {ROUTER_DIRECTIVES} from '@angular/router';
import {CORE_DIRECTIVES} from "@angular/common";

import {QueryService} from '../query.service';
import {ShortestPathComponent} from './shortestpath.component';

@Component({
  templateUrl: 'views/graph/graph.tpl.html',
  directives: [CORE_DIRECTIVES, <any>ShortestPathComponent]
})
export class AlgosComponent {
  constructor() {
  }
}
