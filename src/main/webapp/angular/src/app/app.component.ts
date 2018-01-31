import {Component, OnInit} from '@angular/core';
import {FormControl, FormGroup} from "@angular/forms";
declare var jquery:any;
declare var $ :any;

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {

  public list: Message[] = [];
  loginInfo: FormGroup;
  currentUserName: string;
  currentRoomName: string;
  chat_show: boolean;
  login_show: boolean;
  serviceLocation = 'ws://localhost:8080/chatroom/chat';
  rooms = ['Room1', 'Room2', 'Room3', 'Room4'];
  wsocket: WebSocket;
  inputMessage: string = '';
  alert;

  constructor() {
    this.chat_show = false;
    this.login_show = true;
    this.inputMessage = '';
  }

  ngOnInit() {
    this.loginInfo = new FormGroup({
      userName: new FormControl(''),
      roomName: new FormControl('')
    });

    const loginInfoValueChange$ = this.loginInfo.valueChanges;
    loginInfoValueChange$.subscribe(valChange => {
      this.currentUserName = valChange.userName;
      this.currentRoomName = valChange.roomName;
    })

  };


  onSubmit({value, valid}: { value: LoginInfo, valid: boolean }) {
    this.currentRoomName = value.roomName;
    this.currentUserName = value.userName;

    this.connectToChatServer();

    this.login_show = false;
    this.chat_show = true;

  };

  connectToChatServer() {
    this.wsocket = new WebSocket(this.constructURI(this.serviceLocation, encodeURI(this.currentRoomName), this.currentUserName));
    this.wsocket.onerror = this.onConnectionError;
    this.wsocket.onmessage = this.onMessageReceived;
  };

  onMessageReceived(event) {
    // Parse JSON String to JavaScript Object
    var msg = JSON.parse(event.data); // native API

    // Construct HTML snippet and print to screen
    var messageLine = '<tr><td class="received">' + msg.received.substring(0, 8)
      + '</td><td class="user">' + msg.sender
      + '</td><td class="message">' + msg.content
      + '</td></tr>';
    $('#response').append(messageLine);
    //this.list.push(msg);
  };

  sendMessage() {
    // Construct message to send to server
    let msg = '{"content":"' + this.inputMessage + '", "sender":"' + this.currentUserName + '", "received":"' + '"}';
    this.wsocket.send(msg);
    this.inputMessage = '';
  };

  onConnectionError(event: any) {
    this.alert.append(event);
  };

  leaveRoom() {
    this.wsocket.close();
    this.login_show = true;
    this.chat_show = false;
  };

  constructURI(serviceLocation, room, user) {
    return serviceLocation + "/" + room + "/" + user;
  };

  constructHTMLSnippet(nickName, content, received) {
    return '<tr><td class="received">' + received.substring(0, 8)
      + '</td><td class="user">' + nickName
      + '</td><td class="message">' + content
      + '</td></tr>';
  }
}

export interface LoginInfo {
  userName: string;
  roomName: string;
}

export class Message {
  content: string;
  sender: string;
  received: string;


  constructor(content: string, sender: string, received: string) {
    this.content = content;
    this.sender = sender;
    this.received = received;
  }
}
