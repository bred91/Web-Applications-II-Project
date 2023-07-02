import Chat from './Chat'
import './Ticket.css'
import TicketHandler from "./TicketHandler";
import {useNavigate, useParams} from "react-router-dom";
import {useEffect, useState} from "react";
import * as API from "../API";
import {toast} from 'react-toastify';

function Ticket(props) {

    const {ticketId} = useParams()
    const navigate = useNavigate()
    useEffect(()=>{
        if(!props.ticketID){
            navigate('/tickets');
        }
    },[props.ticketID])

    const [loading, setLoading] = useState(true);


    useEffect(() => {

        API.fetchTicket(props.accessToken, ticketId)
            .then( ticket => props.setTicketDetails(ticket))
            .catch((err) => toast.error(err.message));

            API.fetchMessages(props.accessToken, ticketId)
                .then( allMessages =>{ props.setMessages([...allMessages]); setLoading(false);})
                .catch((err) => toast.error(err.message));

        }, []);

    return (

        <div className="ticket-container">
            <div className="left-container">
                <TicketHandler accessToken={props.accessToken} user={props.user} role={props.role} setTickets={props.setTickets} ticketDetails={props.ticketDetails}/>
            </div>
            <div className="right-container">
                <Chat accessToken={props.accessToken} loading={loading} user={props.user} role={props.role} messages={props.messages}  />
            </div>
        </div>
    );
}

export default Ticket;