import {Nav} from "react-bootstrap";
import {Link} from "react-router-dom";
import React from "react";


function ManagerNavbar(){

    return(
        <Nav className="mx-auto">
            <Nav.Item>
                <Nav.Link as={Link} to="/tickets">
                    Tickets
                </Nav.Link>
            </Nav.Item>
            <Nav.Item>
                <Nav.Link as={Link} to="/searchProfile">
                    Search Profile
                </Nav.Link>
            </Nav.Item>
            <Nav.Item>
                <Nav.Link as={Link} to="/searchProduct">
                    Search Product
                </Nav.Link>
            </Nav.Item>
            <Nav.Item>
                <Nav.Link as={Link} to="/createProduct">
                    Create Product
                </Nav.Link>
            </Nav.Item>
            <Nav.Item>
                <Nav.Link as={Link} to="/createExpert">
                    Create Expert
                </Nav.Link>
            </Nav.Item>
        </Nav>
    )
}

function ExpertNavbar(){

    return(
        <Nav className="mx-auto">
            <Nav.Item>
                <Nav.Link as={Link} to="/tickets">
                    Tickets
                </Nav.Link>
            </Nav.Item>
            <Nav.Item>
                <Nav.Link as={Link} to="/searchProduct">
                    Search Product
                </Nav.Link>
            </Nav.Item>
        </Nav>
    );
}

function ClientNavbar(){

    return(
        <>
            <Nav className="mx-auto">
                <Nav.Item>
                    <Nav.Link as={Link} to="/createTicket">
                        Create Ticket
                    </Nav.Link>
                </Nav.Item>
                <Nav.Item>
                    <Nav.Link as={Link} to="/tickets">
                        Tickets
                    </Nav.Link>
                </Nav.Item>
                <Nav.Item>
                    <Nav.Link as={Link} to="/updateProfile">
                        Update Profile
                    </Nav.Link>
                </Nav.Item>
            </Nav>
        </>
    );
}

export {ManagerNavbar, ExpertNavbar, ClientNavbar};