import { Injectable, OnInit, EventEmitter, Output } from '@angular/core';
import { delay } from 'rxjs/operators'
import { HttpClient } from '@angular/common/http';
import { of } from 'rxjs';
import * as moment from 'moment';

import { Student } from './Student';

@Injectable({
    providedIn: 'root'
})
export class StudentService implements OnInit {

    students = null;

    @Output() createStudentEventEmitter = new EventEmitter<Student>();    

    constructor(private httpClient : HttpClient) {

        this.students = 
        [
            {
                "id": 1,
                "firstname" : "onename",
                "lastname" : "onelastname",
                "birthdate" : moment("1990-01-01")
            },
            {
                "id": 2,
                "firstname" : "twoname",
                "lastname" : "twolastname",
                "birthdate" : moment("1994-01-01")
            }
        
        ];        

    }
    ngOnInit(): void {
    }

    downloadStudents() {
        return of(this.students).pipe(delay(1000))
    }

    createStudent(student : Student) {
        student.id = this.students.length + 1;
        this.students.push(student)
        this.createStudentEventEmitter.emit(student)
    }
}