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


    useEffect(() => {
        API.getTickets(props.accessToken)
            .then((tick) => setTickets(tick))
            .catch((err) => toast.error(err.message));
        fetchMessages(props.accessToken, ticketId)
            .then( allMessages => setMessages([...allMessages]))
            .catch((err) => toast.error(err.message));
    }, []);

    const createMessage = async() => {
        console.log("INVIATO!")
    }

    return (
        <div>
            {messages.map((message) => {
                return <MessageBox key={message.sentTS}
                    position={message.senderId === props.user.email ? 'right' : 'left'}
                    text={message.content.text}
                    title={message.senderId === props.user.email ? 'You' : message.senderId}
                    date={message.sentTS}
                    type={'text'}

                />})
            }
            <Input
                placeholder={"Type a message..."}
                multiline
                value={text}
                onChange={(e) => setText(e.target.value)}
            />

            <Button text="Send" onClick={createMessage} />


        </div>
    )
}

export default Chat