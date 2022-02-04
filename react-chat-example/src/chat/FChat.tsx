import React from 'react'
import './fchat.css'
import { FChatInputBox } from './FChatInputBox';

import SockJS from "sockjs-client";
import Stomp, {Client} from 'webstomp-client';

export class Username {
  value: string

  constructor(value: string) {
    this.value = value
  }
}

export class Message {
  from: Username | string
  body: string

  constructor(from: Username | string, body: string) {
    this.from = from;
    this.body = body;
  }
}

export interface FChatProps {
  nickname: string
}

interface FChatState {
  connected: boolean
  connectionTimeout: number
  messages: Message[]
}

export class FChat extends React.Component<FChatProps, FChatState> {

  private stompClient: Client | undefined

  constructor(props: FChatProps) {
    super(props)
    let messages: Message[] = [
      new Message("user1", "body1"),
      new Message("user1", "body2"),
      new Message("user2", "body3"),
      new Message("user3", "body4")
    ]
    this.state = {
      connected: false,
      connectionTimeout: 0,
      messages: messages
    }

    this.handleSubmit = this.handleSubmit.bind(this);
    this.onSendMessage = this.onSendMessage.bind(this);
    this.reconnectWebSocket = this.reconnectWebSocket.bind(this);
  }

  componentDidMount() {
    this.initWebSocket()
  }

  initWebSocket() {
    const socket = new SockJS('http://localhost:8080/gs-guide-websocket');

    this.stompClient = Stomp.over(socket);
    this.stompClient.debug = () => {}

    const localStompClient = this.stompClient
    const localThis = this;

    this.stompClient.connect({}, function (frame) {
      console.log("connect")
      localThis.setState({ connected: true })

      console.log("localStompClient", localThis.stompClient)
      console.log("frame", frame)
      console.log("transport url: " + localStompClient.ws._transport.url)
      console.log("sessionId: " + localStompClient.ws._transport.unloadRef)

      localStompClient.subscribe('/topic/chat', function (chatMsg) {
        console.log("receiveMessage", chatMsg.body)
        localThis.onReceiveMessage(JSON.parse(chatMsg.body))
      });
    }, function (closeEvent) {
      console.log("close event", closeEvent)
      localThis.setState({ connected: false })
      localThis.reconnectWebSocket()
    });
  }

  reconnectWebSocket() {
    const initReconnectTimeout = 5
    this.setState({ connectionTimeout: initReconnectTimeout })

    const func = () => {
      let t = this.state.connectionTimeout
      t -= 1
      this.setState({connectionTimeout: t } )
      if (t > 0) {
        setTimeout(func, 1000)
      } else {
        this.initWebSocket()
      }
    }
    setTimeout(func, 1000)
  }

  sendMessageViaWebSocket(message: Message) {
    this.stompClient?.send("/app/chat", JSON.stringify(message), {})
  }

  onReceiveMessage(message: Message) {
    const msgs = this.state.messages
    msgs.push(message)
    this.setState({messages: msgs})
  }

  onSendMessage(msgBody: string) {
    console.log('FChat.onSendMessage ' + msgBody)
    this.sendMessageViaWebSocket(new Message(this.props.nickname, msgBody))
  }

  handleSubmit(event: React.FormEvent<HTMLFormElement>) {
    event.preventDefault()
  }

  render() {
    const msgItems = this.state.messages.map((msg, index) =>
      <div key={index}>
        <span>&lt;{msg.from}&gt;</span> {msg.body}
      </div>
    )

    return (
      <form onSubmit={this.handleSubmit}>
        <div>
          connect status: <b>{this.state.connected ? 'connected' : 'disconnected' }</b> {!this.state.connected && <span>(reconnect after {this.state.connectionTimeout} sek)</span> }
        </div>
        <div>
          nick: {this.props.nickname}
        </div>
        <div className="mainWindow">
          <div className="messagesBox">
            {msgItems}
          </div>
          <FChatInputBox onSendMessage={this.onSendMessage}/>
        </div>
      </form>
    )
  }
}

