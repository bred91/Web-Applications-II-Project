import React, { useEffect, useState } from 'react'
import {over} from 'stompjs';
import SockJS from 'sockjs-client';
import {useParams} from "react-router-dom";
import * as API from "../API";
import {fetchMessages} from "../API";
import {toast} from "react-toastify";
import {Button} from "react-chat-elements";

var stompClient =null;
const ChatRoom = () => {
    const [privateChats, setPrivateChats] = useState([]);
    //const [publicChats, setPublicChats] = useState([]);
    //const [tab,setTab] =useState("Tiziot");
    const {ticketId} = useParams()
    const [connected, setConnected] = useState(false)
    const [username,setUsername] = useState('')
    const [file, setFile] = useState(null)
    const [text, setText] = useState('')
    // const [userData, setUserData] = useState({
    //     username: '',
    //     receivername: '',
    //     connected: false,
    //     message: ''
    // });


    const connect =()=>{
        let Sock = new SockJS('http://localhost:8080/ws');
        stompClient = over(Sock);
        stompClient.connect({},onConnected, onError);
    }

    const onConnected = () => {
        //setUserData({...userData,"connected": true});
        //stompClient.subscribe('/chatroom/public', onMessageReceived);
        stompClient.subscribe('/chat/'+ticketId+'/private', onPrivateMessage);
        userJoin();
    }

    const userJoin=()=>{
        var chatMessage = {
            x:"JOINED"
        };
        stompClient.send("/app/message", {}, JSON.stringify(chatMessage));
        setConnected(true)
    }

    const onMessageReceived = (payload)=>{
        var payloadData = JSON.parse(payload.body);
        console.log("RECEIVED = ", payloadData)
        // switch(payloadData.status){
        //     case "JOIN":
        //         if(!privateChats.get(payloadData.senderName)){
        //             privateChats.set(payloadData.senderName,[]);
        //             setPrivateChats(new Map(privateChats));
        //         }
        //         break;
        //     case "MESSAGE":
        //         publicChats.push(payloadData);
        //         setPublicChats([...publicChats]);
        //         break;
        // }
    }

    const onPrivateMessage = (payload)=>{
        console.log("PAYLOAD = ", payload);
        var payloadData = JSON.parse(payload.body);
        console.log("PAYLOAD DATA = ", payloadData)
        //setPrivateChats( privateChats.push)
        // if(privateChats.get(payloadData.senderName)){
        //     privateChats.get(payloadData.senderName).push(payloadData);
        //     setPrivateChats(new Map(privateChats));
        // }else{
            let list =[];
            //list.push(payloadData);
            //privateChats.set(payloadData[text],list);
            let text = payloadData.text;
            list.push(text)
            setPrivateChats([text]);
            console.log(privateChats)
        // }
    }

    const onError = (err) => {
        console.log(err);

    }

    const handleMessage =(event)=>{
        const {value}=event.target;
        //setUserData({...userData,"message": value});
    }
    // const sendValue=()=>{
    //     if (stompClient) {
    //         var chatMessage = {
    //             file:
    //         };
    //         console.log(chatMessage);
    //         stompClient.send("/app/message", {}, JSON.stringify(chatMessage));
    //     }
    // }

    const sendValue = async(e) => {
        e.preventDefault()
        try {

            if (stompClient) {

                if(!(!file && !text)){

                    const formData = new FormData()

                    if(file) {
                        formData.append('file', file)
                    }
                    if(text) {
                        formData.append('text', text)
                    }




                }

                var chatMessage = {
                    file:file,
                    text:text,
                    ticketId
                };
                console.log(chatMessage);
                stompClient.send("/app/private-message", {}, JSON.stringify(chatMessage));

            }




        }
        catch (error) {
            toast.error(error, {position: "bottom-center", autoClose: 2000});
        }


    }

    // const sendPrivateValue=()=>{
    //     if (stompClient) {
    //
    //
    //         // if(userData.username !== tab){
    //         //     privateChats.get(tab).push(chatMessage);
    //         //     setPrivateChats(new Map(privateChats));
    //         // }
    //         stompClient.send("/app/private-message", {}, JSON.stringify(chatMessage));
    //         //setUserData({...userData,"message": ""});
    //     }
    // }

    const handleUsername=(event)=>{
        const {value}=event.target;
        setUsername(value)
        //setUserData({...userData,"username": value});
    }

    const registerUser=()=>{
        connect();
    }
    return (
        <div className="container">
            {connected?
                <div className="chat-box">
                    {/*<div className="member-list">*/}
                    {/*    <ul>*/}
                    {/*        <li onClick={()=>{setTab("CHATROOM")}} className={`member ${tab==="CHATROOM" && "active"}`}>Chatroom</li>*/}
                    {/*        {[...privateChats.keys()].map((name,index)=>(*/}
                    {/*            <li onClick={()=>{setTab(name)}} className={`member ${tab===name && "active"}`} key={index}>{name}</li>*/}
                    {/*        ))}*/}
                    {/*    </ul>*/}
                    {/*</div>*/}
                    {/*{tab==="CHATROOM" && <div className="chat-content">*/}
                    {/*    <ul className="chat-messages">*/}
                    {/*        {publicChats.map((chat,index)=>(*/}
                    {/*            <li className={`message ${chat.senderName === userData.username && "self"}`} key={index}>*/}
                    {/*                {chat.senderName !== userData.username && <div className="avatar">{chat.senderName}</div>}*/}
                    {/*                <div className="message-data">{chat.message}</div>*/}
                    {/*                {chat.senderName === userData.username && <div className="avatar self">{chat.senderName}</div>}*/}
                    {/*            </li>*/}
                    {/*        ))}*/}
                    {/*    </ul>*/}

                    {/*    <div className="send-message">*/}
                    {/*        <input type="text" className="input-message" placeholder="enter the message" value={userData.message} onChange={handleMessage} />*/}
                    {/*        <button type="button" className="send-button" onClick={sendValue}>send</button>*/}
                    {/*    </div>*/}
                    {/*</div>}*/}
                    {/*{tab!=="CHATROOM" && <div className="chat-content">*/}
                    {/*    <ul className="chat-messages">*/}
                    {/*        {[...privateChats.get(tab)].map((chat,index)=>(*/}
                    {/*            <li className={`message ${chat.senderName === userData.username && "self"}`} key={index}>*/}
                    {/*                {chat.senderName !== userData.username && <div className="avatar">{chat.senderName}</div>}*/}
                    {/*                <div className="message-data">{chat.message}</div>*/}
                    {/*                {chat.senderName === userData.username && <div className="avatar self">{chat.senderName}</div>}*/}
                    {/*            </li>*/}
                    {/*        ))}*/}
                    {/*    </ul>*/}


                    {/*</div>}*/}

                    <div className="send-message">
                        <input type="text" className="input-message" placeholder="enter the message" value={username} onChange={handleMessage} />
                        <button type="button" className="send-button" onClick={sendValue}>send</button>
                    </div>
                </div>
                :
                <div className="register">
                    <input
                        id="user-name"
                        placeholder="Enter your name"
                        name="userName"
                        onChange={handleUsername}
                        margin="normal"
                    />
                    <button type="button" onClick={registerUser}>
                        connect
                    </button>
                </div>}
            <div>
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
            </div>

            <div>
                {privateChats.length>0 && privateChats.map((p) => {return <p>{p.text}</p>})}
            </div>
        </div>

    )
}

export default ChatRoom