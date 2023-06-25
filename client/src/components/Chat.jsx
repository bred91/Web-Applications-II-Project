import React, {useState, useEffect } from 'react';
import {fetchMessages} from "../API";
import {MessageBox, Input, Button, MessageList} from "react-chat-elements";
import {useParams} from "react-router-dom";
import {toast} from 'react-toastify';
import * as API from "../API";
import 'react-chat-elements/dist/main.css'


function Chat(props){
    const [messages, setMessages] = useState([])
    const [tickets, setTickets] = useState([])
    const [text, setText] = useState('')
    const [file, setFile] = useState(null)
    const {ticketId} = useParams()
    const messageListReferance = React.createRef();

    const handleFileOpening = (h) => {
        console.log("OPENED")

        window.open(h, "_blank")
    }

    useEffect(() => {
        fetchMessages(props.accessToken, ticketId)
            .then( allMessages => setMessages([...allMessages]))
            .catch((err) => toast.error(err.message));
    }, [text, file]);

    const createMessage = async() => {
        try {
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
        }
         catch (error) {
            console.log("ERROR = "+error)
         }


    }

    return (
        <div>
            {messages.map((message) => {
                return <MessageBox key={message.sentTS}
                    position={message.senderId === props.user.email ? 'right' : 'left'}
                    //text={message.content.text}
                    title={message.senderId === props.user.email ? 'You' : message.senderId}
                    date={message.sentTS}
                               type={'file'}
                               data={{
                                   mimeType:message.content.attachment ? message.content.attachment.contentType : '',
                                   uri:message.content.attachment ? URL.
                                   createObjectURL(
                                       new Blob([message.content.attachment? message.content.attachment.content : null]),
                                       {type:message.content.attachment ? message.content.attachment.content : ''}) : ''

                                }}
                                   onClick={() => handleFileOpening("blob:http://localhost:3007/f9a3bb90-3b8f-46e2-8c8a-0bfc75bff2a5")}


                />})
            }

            <Input
                placeholder={"Type a message..."}
                multiline
                value={text}
                onChange={(e) => setText(e.target.value)}
            />

            <Input
                type='file'
                onChange={(e) => setFile(e.target.value)}

            />

            <Button text="Send" onClick={createMessage} />


        </div>
    )
}

export default Chat