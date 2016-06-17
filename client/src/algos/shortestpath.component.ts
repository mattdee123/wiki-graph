import {Component, Inject, OnInit} from '@angular/core';
import {ROUTER_DIRECTIVES, RouteParams} from '@angular/router-deprecated';
import {CORE_DIRECTIVES, Location} from '@angular/common';

import {QueryService} from "../query.service";
import {UrlencodePipe} from "../pipes";

@Component({
  selector: 'wg-shortest-path',
  templateUrl: 'views/algos/shortestpath.tpl.html',
  directives: [ROUTER_DIRECTIVES],
  pipes: [<any>UrlencodePipe]
})
export class ShortestPathComponent implements OnInit {
  private start:string;
  private end:string;
  private loading:boolean;
  private error:string;
  private result:{
    time: number;
    path: string[];
  };

  constructor(private queryService:QueryService,
              private location:Location,
              private route:RouteParams) {
  }

  ngOnInit() {
    let start = this.route.params['shortestPathStart'];
    let end = this.route.params['shortestPathEnd'];
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
