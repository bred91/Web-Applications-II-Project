import React, { useState, useEffect, Component } from "react";
import { Container, Table} from "react-bootstrap";
import * as API from "../API";
import {toast} from "react-toastify";
import {useNavigate} from "react-router-dom";
import { DataTable, Box, Meter, Text } from 'grommet';

const columns = [
    /*{
        property: 'id',
        header: <Text>ID</Text>,
        primary: true,
        visible: false,
    },*/
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
                {
                    tickets.length > 0 ?
                    <DataTable
                    onClickRow={({datum}) => handleClick(datum.id)}
                    sortable={true}
                    search={true}
                    size='big'
                    columns={columns}
                    primaryKey='id'
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
                        : <p>Loading</p>}
            </Container>
        );

}

export default Tickets;
