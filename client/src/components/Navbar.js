import React from "react";
import { Link } from "react-router-dom";
import { Container, Navbar, Nav } from "react-bootstrap";


function TSNavbar() {
    return (
        <Navbar bg="light">
          <Container>
            <Navbar.Brand href="/">Ticketing Platform</Navbar.Brand>
            <Nav className="me-auto">
                <Nav.Link href="/updateProfile">Update Profile</Nav.Link>
                <Nav.Link href="/searchProfile">Search Profile</Nav.Link>
                <Nav.Link href="/searchProduct">Search Product</Nav.Link>
            </Nav>
            <Navbar.Collapse className="justify-content-end">
                <Link to="/signup">
                    <button className="btn btn-primary"> Signup </button>
                </Link>
            </Navbar.Collapse>
          </Container>
        </Navbar>
      );
}

export default TSNavbar;
