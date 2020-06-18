import { Component, OnInit } from '@angular/core';
import { StudentService } from '../student/student.service';

@Component({
  selector: 'app-students-list',
  templateUrl: './students-list.component.html',
  styleUrls: ['./students-list.component.css']
})
export class StudentsListComponent implements OnInit {

  students;
  displayedColumns: string[] = ['firstname', 'lastname', 'birthdate'];

  constructor(private studentService : StudentService) { 
  }

  ngOnInit(): void {
    this.students = this.studentService.downloadStudents()
    this.studentService.createStudentEventEmitter.subscribe(std => this.students = this.studentService.downloadStudents()
    )
  }

}
