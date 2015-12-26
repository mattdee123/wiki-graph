import {Pipe} from "angular2/core";
import {PipeTransform} from "angular2/core";
import {Injectable} from "angular2/core";

@Pipe({
  name: 'urlencode'
})
@Injectable()
export class UrlencodePipe implements PipeTransform {
  public transform(value: string) {
    return encodeURIComponent(value);
  }
}
