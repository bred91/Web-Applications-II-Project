import React, {useState, useEffect} from 'react';
import {useNavigate, useParams} from "react-router-dom";
import * as API from "../API";
import {toast} from "react-toastify";
import Dropdown from 'react-bootstrap/Dropdown';
import {Form} from 'react-bootstrap'
import './ShowProfile.css'
import './TicketHandler.css'

function TicketHandler(props) {
    const {ticketId} = useParams()
    const [ticketDetails, setTicketDetails] = useState(null)
    const [experts, setExperts] = useState([])
    const [selectedExpert, setSelectedExpert] = useState(null)
    const [selectedPriority, setSelectedPriority] = useState(null)
    const navigate = useNavigate();

    useEffect(() => {

        API.fetchTicket(props.accessToken, ticketId)
            .then( ticket => setTicketDetails(ticket))
            .catch((err) => toast.error(err.message));

        API.fetchExperts(props.accessToken)
        .then( experts => setExperts(experts))
        .catch((err) => toast.error(err, {position: "bottom-center", autoClose: 2000}));
    }, []);

    const assignExpert = () => {

        API.startProgress(props.accessToken, ticketId, selectedExpert, selectedPriority)
            .then( ()=> {
                toast.success("Expert assigned successfully!", {position: "bottom-center", autoClose: 2000})
                API.fetchTicket(props.accessToken, ticketId)
                    .then( ticket => setTicketDetails(ticket))
                    .catch((err) => toast.error(err.message));
            })
            .catch((err) => toast.error(err, {position: "bottom-center", autoClose: 2000}));

    }

    const stopProgress = () => {

        API.stopProgress(props.accessToken, ticketId)
            .then( ()=> toast.success("Progress stopped", {position: "bottom-center", autoClose: 2000}))
            .catch((err) => toast.error(err, {position: "bottom-center", autoClose: 2000}));
        navigate('/tickets');
    }

    const reopenIssue = () => {

        API.reopenIssue(props.accessToken, ticketId)
            .then( ()=> {
                toast.success("Issue reopened", {position: "bottom-center", autoClose: 2000});
                API.fetchTicket(props.accessToken, ticketId)
                    .then( ticket => setTicketDetails(ticket))
                    .catch((err) => toast.error(err.message));
            })
            .catch((err) => toast.error(err, {position: "bottom-center", autoClose: 2000}));

    }

    const resolveIssue = () => {

        API.resolveIssue(props.accessToken, ticketId)
            .then( ()=> {
                toast.success("Issue resolved", {position: "bottom-center", autoClose: 2000});
                API.fetchTicket(props.accessToken, ticketId)
                    .then( ticket => setTicketDetails(ticket))
                    .catch((err) => toast.error(err.message));
            })
            .catch((err) => toast.error(err, {position: "bottom-center", autoClose: 2000}));

    }

    const closeIssue = () => {

        API.closeIssue(props.accessToken, ticketId)
            .then( ()=> {
                toast.success("Issue closed", {position: "bottom-center", autoClose: 2000});
                API.fetchTicket(props.accessToken, ticketId)
                    .then( ticket => setTicketDetails(ticket))
                    .catch((err) => toast.error(err.message));
            })
            .catch((err) => toast.error(err, {position: "bottom-center", autoClose: 2000}));
    }

    return (
        <>
        { ticketDetails && <div>
                <center><div className="col">
                    <button className="button rounded-corners disabled"><strong>TicketId: </strong>{ticketDetails.id}</button>
                </div></center>
                <center><div className="col">
                    <button className="button rounded-corners disabled"><strong>State: </strong>{ticketDetails.state.name.replace("_", " ")}</button>
                </div></center>
                <center><div className="col">
                    <button className="button rounded-corners disabled"><strong>Creation Date: </strong>{ticketDetails.creationDate}</button>
                </div></center>
                <center><div className="col">
                    <button className="button rounded-corners disabled"><strong>Last Modification: </strong>{ticketDetails.lastModification
                        .replace("T", "  ")
                        .split(".")[0]
                        .split(":")
                        .slice(0, -1)
                        .join()
                        .replace(",", ":")}</button>
                </div></center>
                <center><div className="col">
                    <button className="button rounded-corners disabled"><strong>Customer: </strong>{ticketDetails.customer.email}</button>
                </div></center>
                {ticketDetails.actualExpert && <center><div className="col" style={{ height: '4em' }}>
                    <button className="button rounded-corners disabled mb-5"><strong>Expert: </strong>{ticketDetails.actualExpert.email}</button>
                </div></center>}
                {ticketDetails.priorityLevel && props.role!='Client' && <center><div className="col" style={{ height: '4em' }}>
                    <button className="button rounded-corners disabled mb-5"><strong>Priority Level: </strong>{ticketDetails.priorityLevel.name}</button>
                </div></center>}
            <center><div className="col">
            <Dropdown>
                <Dropdown.Toggle variant="custom" className="similar-button" id="dropdown-history">
                   <strong> Ticket History </strong>
                </Dropdown.Toggle>
                <Dropdown.Menu>
                    {ticketDetails.history.sort((a,b)=> a.timestamp > b.timestamp)
                        .map((h, index) => (
                        <Dropdown.Item key={h.timestamp}>
                        <span className="ticket-history-timestamp">
                        {h.timestamp.replace('T', ' ').split('.')[0].split(':').slice(0, -1).join(':')}
                        </span>
                            <span> {h.state.name.replace("_", " ")}</span>
                        </Dropdown.Item>
                    ))}
                </Dropdown.Menu>
            </Dropdown>
            </div></center>
                {
                    props.role=='Client' && (ticketDetails.state.name == "CLOSED" || ticketDetails.state.name == "RESOLVED") &&
                    <center><div className="col">
                        <button onClick={reopenIssue} className="action-button" type="button"><strong>Reopen Issue</strong></button>
                    </div></center>
                }
                {
                    (props.role=='Client' || props.role=='Expert') && ticketDetails.state.name != "CLOSED" && ticketDetails.state.name != "RESOLVED" &&
                    <center><div className="col">
                        <button onClick={resolveIssue} className="action-button" type="button"><strong>Resolve Issue</strong></button>
                    </div></center>
                }

                {
                    (props.role=='Client' || props.role=='Expert' || props.role=='Manager') && ticketDetails.state.name != "CLOSED" &&
                    <center><div className="col">
                        <button onClick={closeIssue} className="action-button" type="button"><strong>Close Issue</strong></button>
                    </div></center>
                }

                {
                    (props.role=='Expert' || props.role=='Manager') && ticketDetails.state.name == "IN_PROGRESS" &&
                    <center><div className="col">
                        <button onClick={stopProgress} className="action-button" type="button"><strong>Stop Progress</strong></button>
                    </div></center>
                }
                {
                    props.role=='Manager' && ticketDetails.state.name == "OPEN" &&
                            <Form>
                                <Form.Select onChange={e => setSelectedExpert(e.target.value)} aria-label="Default select example">
                                    <option>Select an expert</option>
                                    {
                                        experts.map(expert => {return <option key={expert.id} value={expert.id}>{expert.email}</option>} )
                                    }
                                </Form.Select>

                                <Form.Select  onChange={e => setSelectedPriority(e.target.value)} aria-label="Default select example">
                                    <option>Select a prority level</option>
                                    <option value={0}>LOW</option>
                                    <option value={1}>MEDIUM</option>
                                    <option value={2}>HIGH</option>
                                </Form.Select>
                                <center><div className="col">
                                    <button onClick={assignExpert} className="action-button" type="button"><strong>Start Progress</strong></button>
                                </div></center>

                            </Form>

                }
        </div>
        }
        </>
    )


}

export default TicketHandler