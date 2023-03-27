import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { Button, Container, Form } from "react-bootstrap";
import './SignUpForm.css';
import {toast} from 'react-toastify';
import 'react-toastify/dist/ReactToastify.min.css';
import { getProfiles, updateProfile } from '../API';


function UpdateProfileForm(props) {
    const [allProfiles, setAllProfiles] = useState([]);
    const [email, setEmail] = useState(props.profile? props.profile.email : "");
    const [username, setUsername] = useState(props.profile? props.profile.username : "");
    const [name, setName] = useState(props.profile? props.profile.name : "");
    const [surname, setSurname] = useState(props.profile? props.profile.surname : "");
    const [matchingProfiles, setMatchingProfiles] = useState([]);
    const navigate = useNavigate();

    useEffect(() => {
        const fetchProfiles = async() => {
            try{
                const profiles = await getProfiles();
                setAllProfiles(profiles);
            }catch(err){
                console.log(err);
            }
        };
        fetchProfiles();

    }, []);

    const handleSubmit = async (event) => {
        event.preventDefault();
        try{
            await updateProfile(email, username, name, surname);
            toast.success('Profile updated successfully', {position: "top-center"});
            props.setProfile(null);
            navigate('/');
        }catch(err){
            toast.error(err, {position: "top-center"});
        }
    };

    const handleChange = (event)=>{
        setEmail(event.target.value);
        let matchedProfiles = allProfiles.filter((item) => item.email.toLowerCase().includes(event.target.value));
        setMatchingProfiles(matchedProfiles);
        if(matchedProfiles.length===1 && event.target.value===matchedProfiles[0].email){
            setSurname(matchedProfiles[0].surname);
            setName(matchedProfiles[0].name);
            setUsername(matchedProfiles[0].username);
        }else{
            setSurname("");
            setName("");
            setUsername("");
            props.setProfile(null);
        }
    }



    return (
        <Container className="pt-3">
            <Container className="container-wrapper">
            <Form className='p-5' onSubmit={handleSubmit}>
                <h3>Update Profile</h3>
                <Form.Group controlId="email">
                    <Form.Label>Email</Form.Label>
                    <Form.Control type="email" value={email} onChange={handleChange} list="matching-emails"
            />
            <datalist id="matching-emails">
              {matchingProfiles.map((profile) => (<option key={profile.email} value={profile.email} />))}
            </datalist>
                </Form.Group>
                <Form.Group controlId="username">
                    <Form.Label>Username</Form.Label>
                    <Form.Control type='username' value={username} onChange={(event) => setUsername(event.target.value)} />
                </Form.Group> 
                <Form.Group controlId="name">
                    <Form.Label>Name</Form.Label>
                    <Form.Control type='text' value={name} onChange={(event) => setName(event.target.value)}/>
                </Form.Group> 
                <Form.Group controlId="surname">
                    <Form.Label>Surname</Form.Label>
                    <Form.Control type='text' value={surname} onChange={(event) => setSurname(event.target.value)}/>
                </Form.Group> 
                <Button variant="primary" className="w-100" type="submit">Update</Button>
            </Form>
        </Container>
        </Container>
        
    );
}

export default UpdateProfileForm;
