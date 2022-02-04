import React from 'react';

interface FChatInputBoxProps {
  onSendMessage: (msgBody: string) => void
}

interface FChatInputBoxState {
  input: string
}

export class FChatInputBox extends React.Component<FChatInputBoxProps, FChatInputBoxState> {
  constructor(prop: FChatInputBoxProps) {
    super(prop);
    this.state = { input: '' }

    this.handleChange = this.handleChange.bind(this);
    this.handleSendClick = this.handleSendClick.bind(this);
    this.handleKeyPress = this.handleKeyPress.bind(this);
  }

  handleChange(event: React.ChangeEvent<HTMLInputElement>) {
    this.setState({ input: event.target.value })
    event.preventDefault()
  }

  handleSendClick(event: React.MouseEvent<HTMLInputElement>) {
    this.sendMessage()
  }

  handleKeyPress(event: React.KeyboardEvent<HTMLInputElement>) {
    if (event.key === 'Enter') {
      this.sendMessage()
    }
  }

  sendMessage() {
    const body = this.state.input
    this.setState({ input: '' })
    this.props.onSendMessage(body)
  }

  render() {
    return (
      <div className="inputMessage">
        <input type="button" value="send" onClick={this.handleSendClick}/>:
        <input type="text"
               value={this.state.input}
               onChange={this.handleChange}
               onKeyPress={this.handleKeyPress}
        />
      </div>
    )
  }
}
