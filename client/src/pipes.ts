import {Pipe, PipeTransform} from "@angular/core";

@Pipe({name: 'urlencode'})
export class UrlencodePipe implements PipeTransform {
  public transform(value: string) {
    return encodeURIComponent(value);
  }
}
