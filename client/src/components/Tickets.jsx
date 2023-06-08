import React, { useState, useEffect } from "react";
import {Card, Container, ListGroup, Table} from "react-bootstrap";
import * as API from "../API";
import {toast} from "react-toastify";

function Tickets(props) {
    const [tickets, setTickets] = useState([]);

    useEffect(() => {
        API.getTickets(props.token)
            .then((tick) => setTickets(tick))
            .catch((err) => toast.error(err.message));
    }, [tickets]);

    return (
        <Container>
            <Table>
                <thead>
                    <tr>
                        <th>Event</th>
                        <th>Location</th>
                        <th>Date</th>
                        <th>Price</th>
                        <th>Quantity</th>
                        <th>Buy</th>
                    </tr>
                </thead>
                <tbody>
                {tickets.map((ticket) =>
                    <tr key={ticket.id}>
                        <td>{ticket.id}</td>
                    </tr>
                )}

                </tbody>
            </Table>
        </Container>
    );
}


export default Tickets;
