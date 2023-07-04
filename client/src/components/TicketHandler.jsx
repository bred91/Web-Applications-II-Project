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
    const [experts, setExperts] = useState([])
    const [selectedExpert, setSelectedExpert] = useState(null)
    const [selectedPriority, setSelectedPriority] = useState(null)
    const navigate = useNavigate();

    useEffect(() => {

        API.fetchExperts(props.accessToken)
        .then( experts => setExperts(experts))
        .catch((err) => toast.error(err, {position: "bottom-center", autoClose: 2000}));
    }, []);

    const assignExpert = () => {

        API.startProgress(props.accessToken, ticketId, selectedExpert, selectedPriority)
            .then( ()=> {
                toast.success("Expert assigned successfully!", {position: "bottom-center", autoClose: 2000})
            })
            .catch((err) => toast.error(err, {position: "bottom-center", autoClose: 2000}));

    }

    const stopProgress = () => {

        API.stopProgress(props.accessToken, ticketId)
            .then( ()=> toast.success("Progress stopped", {position: "bottom-center", autoClose: 2000}))
            .catch((err) => toast.error(err, {position: "bottom-center", autoClose: 2000}));
        if(props.role=='Expert'){
            props.setTickets((prevTickets) => {
                return prevTickets.filter((t)=>t.id!=ticketId);
            });
            navigate('/tickets');
        }

    }

    const reopenIssue = () => {

        API.reopenIssue(props.accessToken, ticketId)
            .then( ()=> {
                toast.success("Issue reopened", {position: "bottom-center", autoClose: 2000});
            })
            .catch((err) => toast.error(err, {position: "bottom-center", autoClose: 2000}));

    }

    const resolveIssue = () => {

        API.resolveIssue(props.accessToken, ticketId)
            .then( ()=> {
                toast.success("Issue resolved", {position: "bottom-center", autoClose: 2000});
            })
            .catch((err) => toast.error(err, {position: "bottom-center", autoClose: 2000}));

    }

    const closeIssue = () => {

        API.closeIssue(props.accessToken, ticketId)
            .then( ()=> {
                toast.success("Issue closed", {position: "bottom-center", autoClose: 2000});
            })
            .catch((err) => toast.error(err, {position: "bottom-center", autoClose: 2000}));
    }

    return (
        <>
        { props.ticketDetails && <div>
                <center><div className="col">
                    <button className="button rounded-corners disabled"><strong>TicketId: </strong>{props.ticketDetails.id}</button>
                </div></center>
                <center><div className="col">
                    <button className="button rounded-corners disabled"><strong>State: </strong>{props.ticketDetails.state.name.replace("_", " ")}</button>
                </div></center>
                <center><div className="col">
                    <button className="button rounded-corners disabled"><strong>Creation Date: </strong>{props.ticketDetails.creationDate}</button>
                </div></center>
                <center><div className="col">
                    <button className="button rounded-corners disabled"><strong>Last Modification: </strong>{props.ticketDetails.lastModification
                        .replace("T", "  ")
                        .split(".")[0]
                        .split(":")
                        .slice(0, -1)
                        .join()
                        .replace(",", ":")}</button>
                </div></center>
                <center><div className="col">
                    <button className="button rounded-corners disabled"><strong>Customer: </strong>{props.ticketDetails.customer.email}</button>
                </div></center>
                {props.ticketDetails.actualExpert && <center><div className="col" style={{ height: '4em' }}>
                    <button className="button rounded-corners disabled mb-5"><strong>Expert: </strong>{props.ticketDetails.actualExpert.email}</button>
                </div></center>}
                {props.ticketDetails.priorityLevel && props.role!='Client' && <center><div className="col" style={{ height: '4em' }}>
                    <button className="button rounded-corners disabled mb-5"><strong>Priority Level: </strong>{props.ticketDetails.priorityLevel.name}</button>
                </div></center>}
            <center><div className="col">
            <Dropdown>
                <Dropdown.Toggle variant="custom" className="similar-button" id="dropdown-history">
                   <strong> Ticket History </strong>
                </Dropdown.Toggle>
                <Dropdown.Menu>
                    <table className="ticket-history-table">
                        <thead>
                        <tr>
                            <th className="ticket-history-header">Timestamp</th>
                            <th className="ticket-history-header">State</th>
                            <th className="ticket-history-header">Expert</th>
                        </tr>
                        </thead>
                        <tbody>
                        {props.ticketDetails.history
                            .sort((a, b) => a.timestamp > b.timestamp)
                            .map((h, index) => (
                                <tr key={h.timestamp} className="ticket-history-row">
                                    <td className="ticket-history-cell">
                                        {h.timestamp.replace("T", " ").split(".")[0].split(":").slice(0, -1).join(":")}
                                    </td>
                                    <td className="ticket-history-cell">
                                      <span className="ticket-history-state">
                                        {h.state.name.replace("_", " ")}
                                      </span>
                                                            </td>
                                                            <td className="ticket-history-cell">
                                      <span className="ticket-history-expert">
                                        {h.expert && h.expert.username}
                                      </span>
                                    </td>
                                </tr>
                            ))}
                        </tbody>
                    </table>
                </Dropdown.Menu>
            </Dropdown>
            </div></center>
                {
                    props.role=='Client' && (props.ticketDetails.state.name == "CLOSED" || props.ticketDetails.state.name == "RESOLVED") &&
                    <center><div className="col">
                        <button onClick={reopenIssue} className="action-button" type="button"><strong>Reopen Issue</strong></button>
                    </div></center>
                }
                {
                    (props.role=='Client' || props.role=='Expert') && props.ticketDetails.state.name != "CLOSED" && props.ticketDetails.state.name != "RESOLVED" &&
                    <center><div className="col">
                        <button onClick={resolveIssue} className="action-button" type="button"><strong>Resolve Issue</strong></button>
                    </div></center>
                }

                {
                    (props.role=='Client' || props.role=='Expert' || props.role=='Manager') && props.ticketDetails.state.name != "CLOSED" &&
                    <center><div className="col">
                        <button onClick={closeIssue} className="action-button" type="button"><strong>Close Issue</strong></button>
                    </div></center>
                }

                {
                    (props.role=='Expert' || props.role=='Manager') && props.ticketDetails.state.name == "IN_PROGRESS" &&
                    <center><div className="col">
                        <button onClick={stopProgress} className="action-button" type="button"><strong>Stop Progress</strong></button>
                    </div></center>
                }
                {
                    props.role=='Manager' && (props.ticketDetails.state.name == "OPEN" || props.ticketDetails.state.name == "REOPENED") &&
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