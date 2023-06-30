import React, {useState, useEffect, useRef} from 'react';
import {fetchMessages} from "../API";
import {MessageBox, Button, MessageList, Input} from "react-chat-elements";
import {useNavigate, useParams} from "react-router-dom";
import {toast} from 'react-toastify';
import * as API from "../API";
import 'react-chat-elements/dist/main.css'
import './Chat.css'
import SockJS from 'sockjs-client';
import { Stomp } from '@stomp/stompjs';
import {over} from "stompjs";

var stompClient =null;

function Chat(props){
    //const [messages, setMessages] = useState([])
    const [text, setText] = useState('')
    const [file, setFile] = useState(null)
    const {ticketId} = useParams()



    // useEffect(() => {
    //     let Sock = new SockJS('http://localhost:8080/ws');
    //     stompClient = Stomp.over(Sock);
    //     stompClient.connect({},onConnected, onError);
    //
    //     // Clean up the WebSocket connection
    //     return () => {
    //         disconnect();
    //     };
    // }, [ticketId]);
    //
    // const onConnected = () => {
    //     stompClient.subscribe('/chat/'+ticketId+'/messages', onReceived);
    // }
    //
    // const onReceived = (message) => {
    //     console.log("Received message:", message);
    //     const receivedMessage = JSON.parse(message.body);
    //     setMessages((prevMessages) => [...prevMessages, receivedMessage]);
    // }
    //
    // const onError = (error) => {
    //     console.error('Error during WebSocket connection:', error);
    // }
    //
    // const disconnect = () => {
    //     if (stompClient) {
    //         stompClient.disconnect();
    //     }
    // };

    // useEffect(() => {
    //     fetchMessages(props.accessToken, ticketId)
    //         .then( allMessages => setMessages([...allMessages]))
    //         .catch((err) => toast.error(err.message));
    // }, []);

    const createMessage = async(e) => {
        e.preventDefault()
        try {

            if(!(!file && !text)){

                const formData = new FormData()

                if(file) {
                    formData.append('file', file)
                }
                if(text) {
                    formData.append('text', text)
                }

                await API.createMessage(props.accessToken, ticketId, formData)
                setText('')
                setFile(null)
                const fileInput = document.querySelector('.input_file');
                if (fileInput) {
                    fileInput.value = null;
                }
                // fetchMessages(props.accessToken, ticketId)
                //     .then( allMessages => setMessages([...allMessages]))
                //     .catch((err) => toast.error(err, {position: "bottom-center", autoClose: 2000}));
            }


        }
         catch (error) {
             toast.error(error, {position: "bottom-center", autoClose: 2000});
         }


    }




    return (

        <div>
            {props.messages.map((message, index) => {

                function base64ToBytes(base64) {
                    const binString = atob(base64);
                    return Uint8Array.from(binString, (m) => m.codePointAt(0));
                }

                let uri
                let type = 'file'
                if(message.content.attachment){
                    let bytes = base64ToBytes(message.content.attachment.content)
                    const blob = new Blob([bytes], {type: message.content.attachment.contentType })
                    uri = URL.createObjectURL(blob)
                    //console.log(uri)
                    if(message.content.attachment.contentType.startsWith("image")){
                        type='photo'
                    }else if(message.content.attachment.contentType.startsWith("audio")){
                        type='audio'
                    }else if(message.content.attachment.contentType.startsWith("video")){
                        type='video'
                    }
                }



                return <div key={index}>
                    { message.content.attachment && <div>{
                        (type=='file' || type=='photo') && <MessageBox key={message.sentTS+message.content.attachment.contentType}
                    position={message.senderId === props.user.email ? 'right' : 'left'}
                    text={message.content.attachment.contentType.startsWith("image") || message.content.attachment.contentType.startsWith("video")? '' : message.content.attachment.filename}
                    title={message.senderId === props.user.email ? 'You' : message.senderId}
                    date={message.sentTS}

                    type={type}
                    data={{
                       //uri:"data:"+ message.content.attachment.contentType+";base64,"+message.content.attachment.content,
                       uri: uri,
                        width:300,
                       height:150
                    }}

                    //onClick={() => {window.open("data:"+ message.content.attachment.contentType+";base64,"+message.content.attachment.content, '_blank')}}
                    onClick={()=>window.open(uri,"_blank")}
                    />}{
                        type=='video' && <MessageBox
                            position={message.senderId === props.user.email ? 'right' : 'left'}
                            type={"video"}
                            date={message.sentTS}
                            title={message.senderId === props.user.email ? 'You' : message.senderId}
                            data={{
                                //uri: "data:"+ message.content.attachment.contentType+";base64,"+message.content.attachment.content,
                                videoURL: uri,
                                width: 400,
                                height: 300,
                                status: {
                                    click: true,
                                    loading: 0.5,
                                    download: true,
                                },
                            }}
                        />
                    }{
                        type=='audio' && <MessageBox
                            position={message.senderId === props.user.email ? 'right' : 'left'}
                            type={"audio"}
                            date={message.sentTS}
                            title={message.senderId === props.user.email ? 'You' : message.senderId}
                            data={{
                                //uri: "data:"+ message.content.attachment.contentType+";base64,"+message.content.attachment.content,
                                audioURL: uri,

                            }}
                        />
                    }
                    </div>


                    }{
                message.content.text && <MessageBox key={message.sentTS}
                                                          position={message.senderId === props.user.email ? 'right' : 'left'}
                                                          text={message.content.text}
                                                          title={message.senderId === props.user.email ? 'You' : message.senderId}
                                                          date={message.sentTS}

                                                          type={'text'}


                />
                    }
                </div>
            })
            }
            {
                props.role!='Manager' && <div>
                    <textarea
                        placeholder={"Type a message..."}
                        rows={3}
                        value={text}
                        onChange={(e) => setText(e.target.value)}
                    />
                    <p></p>
                    <input
                        className={"input_file"}
                        type='file'
                        onChange={(e) => setFile(e.target.files[0])}

                    />

                    <p></p>
                    <Button className={"sendButton"} text="Send" onClick={(e) => createMessage(e)} />
                </div>
            }
        </div>
    )
}

export default Chat