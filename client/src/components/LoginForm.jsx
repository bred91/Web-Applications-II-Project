import React, { useState } from "react";
import { Button, Container, Form } from "react-bootstrap";
import './SignUpForm.css';
import {toast} from 'react-toastify';
import 'react-toastify/dist/ReactToastify.min.css';
import { login } from '../API';
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
            console.log(json);
            props.setToken(json.access_token);
            const decodedToken = jwt_decode(json.access_token);
            console.log(decodedToken);
            props.setUser(decodedToken.name);
            props.setIsLoggedIn(true);
            toast.success('Login successfully', {position: "top-center"});
            navigate('/');
        }catch(err){
            toast.error('Wrong email and/or password', {position: "top-center"});
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
