import {Injectable} from 'angular2/core';
import {Http} from "angular2/http";
import {Response} from "angular2/http";
import {Observable} from "rxjs/Observable";
import {Inject} from "angular2/core";

declare var URI:any;

@Injectable()
export class QueryService {
  constructor(public http:Http) {
  }

  public getRandomArticles(count:number) {
    let url:string = URI('/api/randomArticle')
      .search({count: count})
      .toString();
    return this.http.get(url);
  }

  public getShortestPath(start:string, end:string) {
    let url:string = URI('/path')
      .search({start: start, end: end})
      .toString();
    return this.http.get(url);
  }

  public getLinks(page:string) {
    let url:string = URI('/links').search({page: page}).toString();
    return this.http.get(url);
  }

  public getGraph(page:string, maxDepth:number, maxDegree:number, maxArticles:number) {
    let url:string = URI('/graph')
      .search({
        page: page,
        maxDepth: maxDepth,
        maxDegree: maxDegree,
        maxArticles: maxArticles
      })
      .toString();
    return this.http.get(url);
  }
}
