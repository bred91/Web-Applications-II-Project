import React from "react";
import { Link } from "react-router-dom";
import { Container, Navbar, Nav } from "react-bootstrap";

function TSNavbar() {
  return (
    <Navbar bg="light">
      <Container>
        <Navbar.Brand href="/">Ticketing Platform</Navbar.Brand>
        <Nav className="mx-auto">
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
