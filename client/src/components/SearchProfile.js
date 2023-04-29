import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { Button, Container, Form } from "react-bootstrap";
import './SignUpForm.css';
import {toast} from 'react-toastify';
import 'react-toastify/dist/ReactToastify.min.css';
import { getProfiles,  getProfileByEmail } from '../API';
import './ShowProfile.css'
import CsvDownloadButton from "react-json-to-csv";


function SearchProfile(props) {
    const [allProfiles, setAllProfiles] = useState([]);
    const [email, setEmail] = useState("");
    const [profile, setProfile] = useState(null);
    const [matchingProfiles, setMatchingProfiles] = useState([]);
    const [showProfile, setShowProfile] = useState(false);
    const navigate = useNavigate();


    useEffect(() => {
        const fetchProfiles = async() => {
            try{
                const profiles = await getProfiles();
                setAllProfiles(profiles);
                setMatchingProfiles(profiles);
            }catch(err){
                console.log(err);
            }
        };
        fetchProfiles();

    }, []);

    const handleSubmit = async (event) => {
        event.preventDefault();
        if (email === "") {
            toast("Please enter an email", {position: "top-center"})
            return;
        }

        try{
            const profile = await getProfileByEmail(email);
            setProfile(profile);
            setShowProfile(true);
            
        }catch(err){
            toast.error(err, {position: "top-center"});
        }
    };

    const handleChange = (event)=>{
        setEmail(event.target.value);
        let matchedProfiles = allProfiles.filter((item) => item.email.toLowerCase().includes(event.target.value));
        setMatchingProfiles(matchedProfiles);
        if(matchedProfiles.length===1 && event.target.value===matchedProfiles[0].email){
            setProfile(matchedProfiles[0]);
            setShowProfile(true);
        }else{
            setProfile(null);
            setShowProfile(false);
        }
    }

    const handleUpdateClick = async() => {
        props.setProfile(profile);
        navigate('/updateProfile');
    }

    const handleDownloadCSV = async (event) => {
        event.preventDefault();
        const csv = document.getElementById('csv');
        csv.click()
    };


    return (
        <Container className="pt-3">
            <Container className="container-wrapper">
                <center>
                    <Form className='p-5' onSubmit={handleSubmit}>
                        <h3>Search Profile</h3>
                        <Form.Group controlId="email">
                            <Form.Label>Email</Form.Label>
                            <Form.Control type="email" value={email} onChange={handleChange} list="matching-emails"/>
                        <datalist id="matching-emails">
                          {matchingProfiles.map((profile) => (<option key={profile.email} value={profile.email} />))}
                        </datalist>
                        </Form.Group>
                        <Button variant="success" className="w-100" type="submit">Search</Button>
                        <Button variant="warning" className="w-100" onClick={handleDownloadCSV}>Download all</Button>
                    </Form>
                </center>
                <CsvDownloadButton id="csv" data={allProfiles} delimiter={","}
                               filename={"AllProfiles.csv"} style={{display:"none"}}/>
               { showProfile && <div>
                <center><div className="col">
                    <button className="button rounded-corners disabled"><strong>Email: </strong>{profile.email}</button>
                </div></center>
                <center><div className="col">
                    <button className="button rounded-corners disabled"><strong>Username: </strong>{profile.username}</button>
                </div></center>
                <center><div className="col">
                    <button className="button rounded-corners disabled"><strong>Name: </strong>{profile.name}</button>
                </div></center>
                <center><div className="col">
                    <button className="button rounded-corners disabled"><strong>Surname: </strong>{profile.surname}</button>
                </div></center>
                <Button variant="primary" className="editButton mb-3" onClick={handleUpdateClick} >Edit</Button>
                </div>}
            </Container>
        </Container>
        
    );
}

export default SearchProfile;
