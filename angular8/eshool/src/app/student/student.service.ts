import { Injectable, OnInit, EventEmitter, Output } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { of } from 'rxjs';

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
                "firstname" : "onename",
                "lastname" : "onelastname",
                "birthdate" : "1990-01-01"
            },
            {
                "firstname" : "twoname",
                "lastname" : "twolastname",
                "birthdate" : "1990-01-01"
            }
        
        ];        

    }
    ngOnInit(): void {
    }

    downloadStudents() {
        return of(this.students)
    }

    createStudent(student : Student) {
        this.students.push(student)
        this.createStudentEventEmitter.emit(student)
    }
}