import {Component, Inject} from '@angular/core';
import {ROUTER_DIRECTIVES} from '@angular/router-deprecated';
import {CORE_DIRECTIVES, Location} from '@angular/common';

import {QueryService} from "../query.service";
import {UrlencodePipe} from "../pipes";

@Component({
  templateUrl: 'views/links/links.tpl.html',
  directives: [ROUTER_DIRECTIVES, CORE_DIRECTIVES],
  pipes: [[UrlencodePipe]]
})
export class LinksComponent {
  private page:string;

  private links:{
    in:string[];
    out:string[];
  };

  private loading:boolean;
  private error:string;

  constructor(private queryService:QueryService) {
  }

  private submit() {
    this.loading = true;
    this.links = null;
    this.error = null;
    this.queryService.getLinks(this.page)
      .subscribe(
        res => {
          this.links = res.json();
          this.loading = false;
        },
        err => {
          this.error = err.text();
          this.loading = false;
        }
      );
  }
}
