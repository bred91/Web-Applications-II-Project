import Chat from './Chat'
import './Ticket.css'
import TicketHandler from "./TicketHandler";

function Ticket(props) {
    return (
        <div className="ticket-container">
            <div className="left-container">
                <TicketHandler accessToken={props.accessToken} user={props.user} role={props.role} />
            </div>
            <div className="right-container">
                <Chat accessToken={props.accessToken} user={props.user} role={props.role} />
            </div>
        </div>
    );
}

export default Ticket;