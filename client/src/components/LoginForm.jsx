import React, { useState } from "react";
import { Button, Container, Form } from "react-bootstrap";
import './SignUpForm.css';
import {toast} from 'react-toastify';
import 'react-toastify/dist/ReactToastify.min.css';
import { login,getProfileByEmail } from '../API';
import { useNavigate } from "react-router-dom";
import jwt_decode from "jwt-decode";

function LoginForm(props) {
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const navigate = useNavigate();

    const handleSubmit = async (event) => {
        event.preventDefault();
        try{
            const json = await login(email, password);
            //console.log(json);
            props.setAccessToken(json.access_token);
            props.setRefreshToken(json.refresh_token);
            const decodedToken = jwt_decode(json.access_token);
            //console.log(decodedToken.resource_access["springboot-keycloak-client"].roles[0]);
            props.setUser(decodedToken.name);
            props.setIsLoggedIn(true);
            props.setRole(decodedToken.resource_access["springboot-keycloak-client"].roles[0]);
            toast.success('Login successfully', {position: "bottom-center", autoClose: 2000});
            if (decodedToken.resource_access["springboot-keycloak-client"].roles[0] === 'Client') {
                const profile = await getProfileByEmail(json.access_token, email);
                //console.log(profile)
                props.setProfile(profile);
            }

            navigate('/');
        }catch(err){
            toast.error('Wrong email and/or password', {position: "bottom-center", autoClose: 2000});
        }
    };

    return (
        <Container className="pt-3">
            <Container className="container-wrapper">
                <Form className='p-5' onSubmit={handleSubmit}>
                    <h3>Login</h3>
                    <Form.Group controlId="email">
                        <Form.Label>Email</Form.Label>
                        <Form.Control type='email' onChange={(event) => setEmail(event.target.value)} />
                    </Form.Group>
                    <Form.Group controlId="password">
                        <Form.Label>Password</Form.Label>
                        <Form.Control type='password' onChange={(event) => setPassword(event.target.value)} />
                    </Form.Group>
                    <Button variant="primary" className="w-100" type="submit">Login</Button>
                    <Button variant="danger" className="w-100" onClick={()=>navigate('/')}>Cancel</Button>
                </Form>
            </Container>
        </Container>
    );
}

export default LoginForm;
