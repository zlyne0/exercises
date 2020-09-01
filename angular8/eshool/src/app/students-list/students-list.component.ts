import { Component, OnInit } from '@angular/core';
import { StudentService } from '../student/student.service';
import { Student } from '../student/Student'
import { SendMessageDialog } from './send-message-dialog/send-message-dialog.component';

import { SelectionModel } from '@angular/cdk/collections'
import { MatTableDataSource } from '@angular/material/table'
import { MatDialog } from '@angular/material/dialog';

@Component({
  selector: 'app-students-list',
  templateUrl: './students-list.component.html',
  styleUrls: ['./students-list.component.css']
})
export class StudentsListComponent implements OnInit {

  displayedColumns: string[] = ['select', 'firstname', 'lastname', 'age'];
  studentsDatasource = new MatTableDataSource<Student>();
  selection = new SelectionModel<Student>(true, [])

  constructor(private studentService : StudentService, private matDialog : MatDialog) { 
  }

  private populateMatStudentDatasource() {
    this.studentService.downloadStudents().subscribe(things => {
      this.studentsDatasource.data = things;
    })
  }

  ngOnInit(): void {
    this.populateMatStudentDatasource();
    this.studentService.createStudentEventEmitter.subscribe(std => this.populateMatStudentDatasource())
  }

  isAllSelected() {
    const numSelected = this.selection.selected.length;
    const numRows = this.studentsDatasource.data.length;
    return numSelected === numRows;    
  }

  masterToggle() {
     this.isAllSelected() ?
        this.selection.clear() :
        this.studentsDatasource.data.forEach(row => this.selection.select(row));
  }

  checkboxLabel(row?: Student): string {
    if (!row) {
      return `${this.isAllSelected() ? 'select' : 'deselect'} all`;
    }
    return `${this.selection.isSelected(row) ? 'deselect' : 'select'} row ${row.id + 1}`;
  }

  sendMsgOpenDialog() {
    let selectedStudents = []

    for (let item of this.selection.selected) {
      selectedStudents.push(item);
    }

    const dialogDef = this.matDialog.open(SendMessageDialog, { 
      width: '450px', 
      data: { 
        name: 'some name', 
        students: selectedStudents
      } 
    });
    dialogDef.afterClosed().subscribe(result => {
      console.log('Dialog result: ' + result);
    });
  }
}
