import React, {useState} from "react";
import { useNavigate } from "react-router-dom";
import { Button, Container, Form } from "react-bootstrap";
import './SignUpForm.css';
import {toast} from 'react-toastify';
import 'react-toastify/dist/ReactToastify.min.css';
import { updateProfile } from '../API';

function UpdateProfileForm(props) {
    const [email, setEmail] = useState(props.profile? props.profile.email : "");
    const [username, setUsername] = useState(props.profile? props.profile.username : "");
    const [name, setName] = useState(props.profile? props.profile.name : "");
    const [surname, setSurname] = useState(props.profile? props.profile.surname : "");
    const [phoneNumber, setPhoneNumber] = useState(props.profile? props.profile.phoneNumber : "");
    const navigate = useNavigate();



    const handleSubmit = async (event) => {
        event.preventDefault();
        try{
            await updateProfile(props.token,email, username, name, surname, phoneNumber);
            toast.success('Profile updated successfully', {position: "bottom-center", autoClose: 2000});

            const profile = {email: email, username: username, name: name, surname: surname, phoneNumber: phoneNumber};
            props.setProfile(profile);
            navigate('/');
        }catch(err){
            toast.error(err, {position: "bottom-center", autoClose: 2000});
        }
    };


    return (
        <Container className="pt-3">
            <Container className="container-wrapper">
            <Form className='p-5' onSubmit={handleSubmit}>
                <h3>Update Profile</h3>
                <Form.Group controlId="email">
                    <Form.Label>Email</Form.Label>
                    <Form.Control type="email" value={email} disabled list="matching-emails"
            />
                </Form.Group>
                <Form.Group controlId="username">
                    <Form.Label>Username</Form.Label>
                    <Form.Control type='username' value={username} disabled onChange={(event) => setUsername(event.target.value)} />
                </Form.Group> 
                <Form.Group controlId="name">
                    <Form.Label>Name</Form.Label>
                    <Form.Control type='text' value={name} onChange={(event) => setName(event.target.value)}/>
                </Form.Group> 
                <Form.Group controlId="surname">
                    <Form.Label>Surname</Form.Label>
                    <Form.Control type='text' value={surname} onChange={(event) => setSurname(event.target.value)}/>
                </Form.Group>
                <Form.Group controlId="phoneNumber">
                    <Form.Label>Phone Number</Form.Label>
                    <Form.Control type='text' value={phoneNumber} onChange={(event) => setPhoneNumber(event.target.value)}/>
                </Form.Group>
                <Button variant="primary" className="w-100" type="submit">Update</Button>
            </Form>
        </Container>
        </Container>
        
    );
}

export default UpdateProfileForm;
