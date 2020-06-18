import { Pipe, PipeTransform } from '@angular/core'
import * as moment from 'moment';

@Pipe({name : 'age'})
export class AgePipe implements PipeTransform {

    transform(value: moment.Moment, ...args: any[]): number {
        let now = moment();
        return now.year() - value.year()
    }
}