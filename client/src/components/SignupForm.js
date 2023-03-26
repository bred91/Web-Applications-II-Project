import React, { useState } from "react";
import { Button, Container, Form } from "react-bootstrap";
import './SignUpForm.css';
import {toast} from 'react-toastify';
import 'react-toastify/dist/ReactToastify.min.css';

function SignupForm() {
    const [email, setEmail] = useState("");
    const [username, setUsername] = useState("");
    const [name, setName] = useState("");
    const [surname, setSurname] = useState("");

    const handleSubmit = (event) => {
        event.preventDefault();
        fetch("http://localhost:3003/api/profiles/", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify({ email, username, name, surname }),
        })
            .then((response) => {
                if(response.ok){
                    toast.success('Profile created successfully', { position: "top-center" });
                }else{
                    response.json().then((data)=>{
                        console.log(data);
                        toast.error(data.detail, { position: "top-center" })
                    }).catch((error)=>console.log(error));
                    
                }

            });
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
                <Button variant="primary" className="w-100" type="submit">Sign Up</Button>
            </Form>
        </Container>
        </Container>
        
    );
}

export default SignupForm;
