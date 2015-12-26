import {Injectable} from 'angular2/core';
import {Http} from "angular2/http";
import {Response} from "angular2/http";
import {Observable} from "rxjs/Observable";
import {Inject} from "angular2/core";

@Injectable()
export class QueryService {
  constructor(public http: Http) {}

  public getRandomArticles(count: number) {
    let url = '/api/randomArticle?count=' + count;
    return this.http.get(url);
  }

  public getShortestPath(start: string, end: string) {
    start = encodeURIComponent(start);
    end = encodeURIComponent(end);
    let url = `/path?start=${start}&end=${end}`;
    return this.http.get(url);
  }

  public getLinks(page: string) {
    page = encodeURIComponent(page);
    let url = `/links?page=${page}`;
    return this.http.get(url);
  }
}
