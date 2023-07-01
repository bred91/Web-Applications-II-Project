import Chat from './Chat'
import './Ticket.css'
import TicketHandler from "./TicketHandler";
import {useParams} from "react-router-dom";
import {useEffect, useState} from "react";
import SockJS from "sockjs-client";
import {Stomp} from "@stomp/stompjs";
import * as API from "../API";
import {toast} from 'react-toastify';

var stompClient =null;
function Ticket(props) {

    const {ticketId} = useParams()
    const [messages, setMessages] = useState([])
    const [ticketDetails, setTicketDetails] = useState(null)
    const [loading, setLoading] = useState(true);
    const [subscriptions, setSubscriptions] = useState(new Map());




    useEffect(() => {
        let Sock = new SockJS('http://localhost:8080/ws');
        stompClient = Stomp.over(Sock);
        stompClient.connect({},onConnected, onError);

        // Clean up the WebSocket connection
        return () => {
            disconnect();
        };
    }, [ticketId]);

    const onConnected = () => {
        const messagesSubscription = stompClient.subscribe('/topics/'+ticketId+'/messages', onReceivedMessage);
        const ticketSubscription = stompClient.subscribe('/topics/'+ticketId+'/ticket', onUpdateTicket);
        const newSubscriptions = new Map(subscriptions);
        newSubscriptions.set('messages', messagesSubscription);
        newSubscriptions.set('ticket', ticketSubscription);
        setSubscriptions(newSubscriptions);
    };

    const onUpdateTicket = (updatedTicket) => {
        const receivedMessage = JSON.parse(updatedTicket.body);
        setTicketDetails(receivedMessage);
    }
    const onReceivedMessage = (message) => {
        const receivedMessage = JSON.parse(message.body);
        setMessages((prevMessages) => [...prevMessages, receivedMessage]);
    }

    const onError = (error) => {
        console.error('Error during WebSocket connection:', error);
    }

    const disconnect = () => {
        if (stompClient) {
            unsubscribe()
            stompClient.disconnect();
        }
    };

    const unsubscribe = () => {

        subscriptions.forEach((subscription) => {
            subscription.unsubscribe();
        });
        setSubscriptions(new Map());
    };


    useEffect(() => {
        API.fetchTicket(props.accessToken, ticketId)
            .then( ticket => setTicketDetails(ticket))
            .catch((err) => toast.error(err.message));

            API.fetchMessages(props.accessToken, ticketId)
                .then( allMessages =>{ setMessages([...allMessages]); setLoading(false);})
                .catch((err) => toast.error(err.message));

        }, []);

    return (

        <div className="ticket-container">
            <div className="left-container">
                <TicketHandler accessToken={props.accessToken} user={props.user} role={props.role} ticketDetails={ticketDetails}/>
            </div>
            <div className="right-container">
                <Chat accessToken={props.accessToken} user={props.user} role={props.role} messages={messages} loading={loading} />
            </div>
        </div>
    );
}

export default Ticket;