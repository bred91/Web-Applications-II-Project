import React, { useState, useEffect, Component } from "react";
import { Container, Table} from "react-bootstrap";
import * as API from "../API";
import {toast} from "react-toastify";
import {useNavigate} from "react-router-dom";
import { DataTable, Box, Meter, Text } from 'grommet';



/*function Ticket(props) {
    const [tickets, setTickets] = useState([]);
    const navigate = useNavigate();

    useEffect(() => {
        API.getTickets(props.accessToken)
            .then((tick) => setTickets(tick))
            .catch((err) => toast.error(err.message));
    }, []);

    const handleClick = (id) => {
        navigate('/ticket/' + id);
    }

    const size = props.role !== 'Client' ? "col-sm-8" : "col-sm-7";
    return (
        <Container className="mt-5">
                <Table striped bordered hover className={size}>
                    <thead>
                    <tr>
                        <th>Product</th>
                        <th>Creation Date</th>
                        <th>State</th>
                        <th>Last Modification</th>
                        {props.role !== 'Client' ?
                            <th>Priority</th>
                            : null}
                        <th>Assigned To</th>
                        <th>Warranty Expiration Date</th>
                        <th>Open</th>
                    </tr>
                    </thead>
                    <tbody>
                    {tickets.map((ticket) =>
                        <tr key={ticket.id} onClick={handleClick(ticket.id)}>
                            <td>{ticket.purchase.product.name}</td>
                            <td>{ticket.creationDate}</td>
                            <td>{ticket.state.name.replace("_"," ")}</td>
                            <td>{ticket.lastModification.replace("T","  ").split(".")[0].split(":").slice(0,-1).join().replace(",",":")}</td>
                            {props.role !== 'Client' ?
                                <td>{ticket.priorityLevel ? ticket.priorityLevel.name : ''}</td>
                                : null}
                            <td>{ticket.actualExpert ? ticket.actualExpert.name + " " + ticket.actualExpert.surname : ''}</td>
                            <td>{ticket.purchase.expiringDate}</td>
                            <td>
                                <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="cornflowerblue"
                                     className="bi bi-arrow-right-circle-fill" viewBox="0 0 16 16">
                                    <path
                                        d="M8 0a8 8 0 1 1 0 16A8 8 0 0 1 8 0zM4.5 7.5a.5.5 0 0 0 0 1h5.793l-2.147 2.146a.5.5 0 0 0 .708.708l3-3a.5.5 0 0 0 0-.708l-3-3a.5.5 0 1 0-.708.708L10.293 7.5H4.5z"/>
                                </svg>
                                {/!*<Button variant={"outline-light"}><i className="bi bi-arrow-right" style={{color: "cornflowerblue"}}></i></Button>*!/}
                            </td>
                        </tr>
                    )}
                    </tbody>
                </Table>
        </Container>
    );
}*/

const columns = [
    {
        property: 'id',
        header: <Text>ID</Text>,
        primary: true,
        visible: false,
    },
    {
        property: 'product',
        header: 'Product',
    },
    {
        property: 'creationDate',
        header: <Text>Creation Date</Text>,
    },
    {
        property: 'name',
        header: 'Name',
    },
    {
        property: 'lastModification',
        header: 'Last Modification Date',
    },
    {
        property: 'priorityLevel',
        header: 'Priority Level',
    },
    {
        property: 'actualExpert',
        header: 'Actual Expert',
    },
    {
        property: 'expiringDate',
        header: 'Warranty Expiration Date',
    },
];

function Tickets(props){
    const [tickets, setTickets] = useState([]);
    const navigate = useNavigate();

    useEffect(() => {
        API.getTickets(props.accessToken)
            .then((tick) => setTickets(tick))
            .catch((err) => toast.error(err.message));
    }, []);

    const handleClick = (id) => {
        //console.log(id)
        navigate('/ticket/' + id);
    }
        return (
            <Container className="mt-5">
                <DataTable
                    onClickRow={({datum}) => handleClick(datum.id)}
                    sortable={true}
                    search={true}
                    size='big'
                    columns={columns}
                    data={
                        tickets.map((ticket) => {
                            return {
                                id: ticket.id,
                                product: ticket.purchase.product.name,
                                creationDate: ticket.creationDate,
                                name: ticket.state.name.replace("_"," "),
                                lastModification: ticket.lastModification.replace("T","  ").split(".")[0].split(":").slice(0,-1).join().replace(",",":"),
                                priorityLevel: ticket.priorityLevel ? ticket.priorityLevel.name : '',
                                actualExpert: ticket.actualExpert ? ticket.actualExpert.name + " " + ticket.actualExpert.surname : '',
                                expiringDate: ticket.purchase.expiringDate
                            }
                        })
                    }
                />
            </Container>
        );

}

export default Tickets;
