import React from "react";
import { Link } from "react-router-dom";
import {Container, Navbar, Nav, Button} from "react-bootstrap";
import {logout} from "../API";
import { useNavigate } from "react-router-dom";
import {toast} from 'react-toastify';
import 'react-toastify/dist/ReactToastify.min.css';

function TSNavbar(props) {

  const navigate = useNavigate();

  const handleLogout = async () => {
    try {
      await logout(props.accessToken, props.refreshToken);
      //console.log('Logged out successfully')
      props.setIsLoggedIn(false);
      props.setAccessToken(null);
      props.setRefreshToken(null);
      props.setUser(null);
      props.setRole(null);
      navigate('/');
      toast.success('Logged out successfully', {position: "bottom-center", autoClose: 2000});
    } catch (err) {
      toast.error(err, {position: "bottom-center", autoClose: 2000});
    }
  }


  return (
      <Navbar bg="light">
        <Container>
          <Navbar.Brand href="/">Ticketing Platform</Navbar.Brand>
          {props.role === 'Manager'?
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
              </Nav> :
              props.role === 'Client'?
                  <Nav className="mx-auto">
                    <Nav.Item>
                      <Nav.Link as={Link} to="/tickets">
                        Tickets
                      </Nav.Link>
                    </Nav.Item>
                  </Nav>
              :
              null }
          <Navbar.Collapse className="justify-content-end">
            {props.isLoggedIn ?
                <>
                  <Navbar.Text className="mx-2"> Signed in as: {props.user} </Navbar.Text>
                  <Button className="btn btn-danger mx-1" onClick={() => handleLogout()}>
                    Logout
                  </Button>
                </>
                :
                <>
                  <Link to="/signup">
                    <button className="btn btn-primary mx-1"> Signup </button>
                  </Link>
                  <Link to="/login">
                    <button className="btn btn-secondary mx-1"> Login </button>
                  </Link>
                </>
            }
          </Navbar.Collapse>
        </Container>
      </Navbar>
  );
}

export default TSNavbar;
