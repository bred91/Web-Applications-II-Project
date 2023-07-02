import {Nav} from "react-bootstrap";
import {Link} from "react-router-dom";
import React from "react";


function ManagerNavbar(props){

    return(
        <Nav className="mx-auto">
            <Nav.Item>
                <Nav.Link as={Link} to="/tickets" onClick={() => props.setTicketId(null)}>
                    Tickets
                </Nav.Link>
            </Nav.Item>
            <Nav.Item>
                <Nav.Link as={Link} to="/searchProfile" onClick={() => props.setTicketId(null)}>
                    Search Profile
                </Nav.Link>
            </Nav.Item>
            <Nav.Item>
                <Nav.Link as={Link} to="/searchProduct" onClick={() => props.setTicketId(null)}>
                    Search Product
                </Nav.Link>
            </Nav.Item>
            <Nav.Item>
                <Nav.Link as={Link} to="/createProduct" onClick={() => props.setTicketId(null)}>
                    Create Product
                </Nav.Link>
            </Nav.Item>
            <Nav.Item>
                <Nav.Link as={Link} to="/createExpert" onClick={() => props.setTicketId(null)}>
                    Create Expert
                </Nav.Link>
            </Nav.Item>
            <Nav.Item>
                <Nav.Link as={Link} to="/monitoring" onClick={() => props.setTicketId(null)}>
                    Monitoring
                </Nav.Link>
            </Nav.Item>
        </Nav>
    )
}

function ExpertNavbar(props){

    return(
        <Nav className="mx-auto">
            <Nav.Item>
                <Nav.Link as={Link} to="/tickets" onClick={() => props.setTicketId(null)}>
                    Tickets
                </Nav.Link>
            </Nav.Item>
            <Nav.Item>
                <Nav.Link as={Link} to="/searchProduct" onClick={() => props.setTicketId(null)}>
                    Search Product
                </Nav.Link>
            </Nav.Item>
        </Nav>
    );
}

function ClientNavbar(props){

    return(
        <>
            <Nav className="mx-auto">
                <Nav.Item>
                    <Nav.Link as={Link} to="/createTicket">
                        Create Ticket
                    </Nav.Link>
                </Nav.Item>
                <Nav.Item>
                    <Nav.Link as={Link} to="/tickets" onClick={() => props.setTicketId(null)} >
                        Tickets
                    </Nav.Link>
                </Nav.Item>
                <Nav.Item>
                    <Nav.Link as={Link} to="/updateProfile" onClick={() => props.setTicketId(null)}>
                        Update Profile
                    </Nav.Link>
                </Nav.Item>
            </Nav>
        </>
    );
}

export {ManagerNavbar, ExpertNavbar, ClientNavbar};