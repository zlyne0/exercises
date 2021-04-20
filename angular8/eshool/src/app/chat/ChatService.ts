import { Injectable, OnInit } from '@angular/core';
import { Observable, Subject } from 'rxjs';

import { webSocket, WebSocketSubject } from 'rxjs/webSocket';

@Injectable({
    providedIn: 'root'
})
export class ChatService implements OnInit {

    myWebSocket: WebSocketSubject<any>;

    constructor() {
        this.myWebSocket = webSocket('ws://localhost:8080/chatHandler/websocket');

        this.myWebSocket.asObservable().subscribe(
            dataFromServer => {
                console.log("Receive message from socket ", dataFromServer);
            },
            err => console.log("error: ", err),
            () => console.log("complete")
        );

        this.myWebSocket.subscribe(
            msg => console.log('message received: ' + msg), 
            // Called whenever there is a message from the server    
            err => console.log(err), 
            // Called if WebSocket API signals some kind of error    
            () => console.log('complete') 
            // Called when connection is closed (for whatever reason)  
        );        
    }

    ngOnInit(): void {
    }

    sendMsg(nick, msg) {
        this.myWebSocket.next({
            chatMessage: {
                nick: nick,
                msg: msg
            }
        });
    }

}