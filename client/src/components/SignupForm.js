import React, { useState } from "react";
import { Button, Container, Form } from "react-bootstrap";
import './SignUpForm.css';
import {toast} from 'react-toastify';
import 'react-toastify/dist/ReactToastify.min.css';
import { createProfile } from './../API';
import { useNavigate } from "react-router-dom";

function SignupForm() {
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [username, setUsername] = useState("");
    const [name, setName] = useState("");
    const [surname, setSurname] = useState("");
    const [phoneNumber, setPhoneNumber] = useState("")
    const navigate = useNavigate();

    const handleSubmit = async (event) => {
        event.preventDefault();
        try{
            await createProfile(email, password, username, name, surname, phoneNumber);
            toast.success('Profile created successfully', {position: "bottom-center", autoClose: 2000});
            navigate('/login');
        }catch(err){
            toast.error(err, {position: "bottom-center", autoClose: 2000});
        }
    };

    return (
        <Container className="pt-3">
            <Container className="container-wrapper">
            <Form className='p-5' onSubmit={handleSubmit}>
                <h3>Sign Up</h3>
                <Form.Group controlId="email">
                    <Form.Label>Email</Form.Label>
                    <Form.Control type='email' onChange={(event) => setEmail(event.target.value)} />
                </Form.Group>
                <Form.Group controlId="password">
                    <Form.Label>Password</Form.Label>
                    <Form.Control type='password' onChange={(event) => setPassword(event.target.value)} />
                </Form.Group>
                <Form.Group controlId="username">
                    <Form.Label>Username</Form.Label>
                    <Form.Control type='username' onChange={(event) => setUsername(event.target.value)} />
                </Form.Group> 
                <Form.Group controlId="name">
                    <Form.Label>Name</Form.Label>
                    <Form.Control type='name' onChange={(event) => setName(event.target.value)}/>
                </Form.Group> 
                <Form.Group controlId="surname">
                    <Form.Label>Surname</Form.Label>
                    <Form.Control type='name' onChange={(event) => setSurname(event.target.value)}/>
                </Form.Group>
                <Form.Group controlId="phoneNumber">
                    <Form.Label>Phone number</Form.Label>
                    <Form.Control type='name' onChange={(event) => setPhoneNumber(event.target.value)}/>
                </Form.Group>
                <Button variant="primary" className="w-100" type="submit">Sign Up</Button>
            </Form>
        </Container>
        </Container>
        
    );
}

export default SignupForm;
