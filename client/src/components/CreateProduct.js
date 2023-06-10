import React, { useState } from "react";
import { Button, Container, Form } from "react-bootstrap";
import './SignUpForm.css';
import {toast} from 'react-toastify';
import 'react-toastify/dist/ReactToastify.min.css';
import {createProduct} from './../API';
import { useNavigate } from "react-router-dom";

function CreateProduct(props) {
    const [ean, setEan] = useState("");
    const [name, setName] = useState("");
    const [brand, setBrand] = useState("");
    const navigate = useNavigate();

    const handleSubmit = async (event) => {
        event.preventDefault();
        try{
            await createProduct(props.token, ean, name, brand);
            toast.success('Product created successfully', {position: "top-center"});
            navigate('/');
        }catch(err){
            toast.error(err, {position: "top-center"});
        }
    };

    return (
        <Container className="pt-3">
            <Container className="container-wrapper">
                <Form className='p-5' onSubmit={handleSubmit}>
                    <h3>Create Product</h3>
                    <Form.Group controlId="ean">
                        <Form.Label>Ean</Form.Label>
                        <Form.Control type='text' onChange={(event) => setEan(event.target.value)} />
                    </Form.Group>
                    <Form.Group controlId="name">
                        <Form.Label>Name</Form.Label>
                        <Form.Control type='text' onChange={(event) => setName(event.target.value)}/>
                    </Form.Group>
                    <Form.Group controlId="brand">
                        <Form.Label>Brand</Form.Label>
                        <Form.Control type='text' onChange={(event) => setBrand(event.target.value)}/>
                    </Form.Group>
                    <Button variant="primary" className="w-100" type="submit">Create</Button>
                </Form>
            </Container>
        </Container>

    );
}

export default CreateProduct;
