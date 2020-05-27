import { Component, OnInit } from '@angular/core';
import { FormBuilder } from '@angular/forms';
import { Student } from './Student';
import { StudentService } from '../student/student.service';

@Component({
  selector: 'app-student',
  templateUrl: './student.component.html',
  styleUrls: ['./student.component.css']
})
export class StudentComponent implements OnInit {

  studentForm

  constructor(private formBuilder : FormBuilder, private studentService : StudentService) { 
    this.studentForm = this.formBuilder.group({
      firstname: '',
      lastname: '',
      birthdate: ''
    });    
  }

  ngOnInit(): void {
  }

  onSubmit(studentData : Student) {
    console.log('student form submited', studentData)
    
    this.studentService.createStudent(studentData)
    this.studentForm.reset();
  }

}
