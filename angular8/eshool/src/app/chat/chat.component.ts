import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';

@Component({
  selector: 'app-chat',
  templateUrl: './chat.component.html',
  styleUrls: ['./chat.component.css']
})
export class ChatComponent implements OnInit {

  chatForm : FormGroup;

  constructor(private formBuilder: FormBuilder) {
    this.chatForm = this.formBuilder.group({
      'messages': '',
      'inputText': ''
    });
  }

  ngOnInit(): void {
  }

  onSubmit(chatFormData: any) {
    console.log(chatFormData)
  }

  onSendMsg(chatFormData: any) {
    var allMsgs = this.chatForm.controls['messages'].value;
    var newMsg = '<nick> ' + chatFormData['inputText'];
    this.chatForm.controls['messages'].setValue(allMsgs + '\r\n' + newMsg);

    this.chatForm.controls['inputText'].setValue('');
  }

}
