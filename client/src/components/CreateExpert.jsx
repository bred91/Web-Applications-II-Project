import React, { useState } from "react";
import { Button, Container, Form } from "react-bootstrap";
import './SignUpForm.css';
import {toast} from 'react-toastify';
import 'react-toastify/dist/ReactToastify.min.css';
import { createExpert } from '../API';
import { useNavigate } from "react-router-dom";

function CreateExpert(props) {
    const [email, setEmail] = useState("");
    const [username, setUsername] = useState("");
    const [name, setName] = useState("");
    const [surname, setSurname] = useState("");
    const [password, setPassword] = useState("");
    const navigate = useNavigate();

    const handleSubmit = async (event) => {
        event.preventDefault();
        try{
            await createExpert(email, username, name, surname, password, props.token);
            toast.success('Expert created successfully', {position: "top-center"});
            navigate('/');
        }catch(err){
            toast.error(err, {position: "top-center"});
        }
    };

    return (
        <Container className="pt-3">
            <Container className="container-wrapper">
                <Form className='p-5' onSubmit={handleSubmit}>
                    <h3>Create an Expert</h3>
                    <Form.Group controlId="email">
                        <Form.Label>Email</Form.Label>
                        <Form.Control type='email' onChange={(event) => setEmail(event.target.value)} />
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
                    <Form.Group controlId="password">
                        <Form.Label>Password</Form.Label>
                        <Form.Control type='password' minLength={0} onChange={(event) => setPassword(event.target.value)}/>
                    </Form.Group>
                    <Button variant="primary" className="w-100" type="submit">Sign Up</Button>
                </Form>
            </Container>
        </Container>
    );
}

export default CreateExpert;
