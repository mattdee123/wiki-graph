import {Component, Inject} from 'angular2/core';
import {ROUTER_DIRECTIVES, Location, RouteParams} from 'angular2/router';
import {CORE_DIRECTIVES} from 'angular2/common';

import {QueryService} from "../query.service";
import {UrlencodePipe} from "../pipes";

@Component({
  selector: 'wg-shortest-path',
  templateUrl: 'views/algos/shortestpath.tpl.html',
  directives: [ROUTER_DIRECTIVES],
  pipes: [UrlencodePipe]
})
export class ShortestPathComponent {
  private start:string;
  private end:string;
  private loading:boolean;
  private error:string;
  private result: {
    time: number;
    path: string[];
  };

  constructor(private queryService:QueryService,
              private location:Location,
              private params:RouteParams) {
    let start = params.get('shortestPathStart');
    let end = params.get('shortestPathEnd');
    if (start && end) {
      this.start = decodeURIComponent(start);
      this.end = decodeURIComponent(end);
      this.submit();
    }
  }

  private randomize() {
    this.loading = true;
    this.queryService.getRandomArticles(2)
      .subscribe(res => {
        let articles = res.json();
        this.start = articles[0];
        this.end = articles[1];
        this.loading = false;
        this.submit();
      });
  }

  private submit() {
    this.loading = true;
    this.result = null;
    this.error = null;
    this.queryService.getShortestPath(this.start, this.end)
      .subscribe(
        res => {
          this.result = res.json();
        },
        err => {
          this.error = err.text();
        },
        () => {
          let start = encodeURIComponent(this.start);
          let end = encodeURIComponent(this.end);
          this.location.replaceState('/algos', `shortestPathStart=${start}&shortestPathEnd=${end}`);
          this.loading = false;
        }
      )
    ;
  }

  private swap() {
    let tmp = this.start;
    this.start = this.end;
    this.end = tmp;
    this.submit();
  }
}
