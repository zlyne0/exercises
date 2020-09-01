import { Component, OnInit, Inject } from '@angular/core';
import { MatDialog, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { Student } from '../../student/Student'


export interface DialogData {
  animal: string;
  students: Student[];
}

@Component({
  selector: 'send-message-dialog',
  templateUrl: './send-message-dialog.component.html',
  styleUrls: ['./send-message-dialog.component.css']
})
export class SendMessageDialog implements OnInit {

  constructor(
    public dialogRef: MatDialogRef<SendMessageDialog>,
    @Inject(MAT_DIALOG_DATA) public data: DialogData
  ) { 
  }

  ngOnInit(): void {
  }

  onCancel(): void {
    this.dialogRef.close('some result data onCancel')
  }

  onOk() : void {
    this.dialogRef.close('some result data onOk')
  }

}
