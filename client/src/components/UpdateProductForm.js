import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { Button, Container, Form } from "react-bootstrap";
import './SignUpForm.css';
import {toast} from 'react-toastify';
import 'react-toastify/dist/ReactToastify.min.css';
import {getProduct, updateProduct} from '../API';


function UpdateProductForm(props) {
    const [allProduct, setAllProduct] = useState([]);
    const [ean, setEan] = useState(props.product? props.product.ean : "");
    const [name, setName] = useState(props.product? props.product.name : "");
    const [brand, setBrand] = useState(props.product? props.product.brand : "");
    const [matchingProduct, setMatchingProduct] = useState([]);
    const navigate = useNavigate();

    useEffect(() => {
        const fetchProduct = async() => {
            try{
                const product = await getProduct(props.token);
                setAllProduct(product);
            }catch(err){
                console.log(err);
            }
        };
        fetchProduct();

    }, []);

    const handleSubmit = async (event) => {
        event.preventDefault();
        try{
            await updateProduct(props.token, ean, name, brand);
            toast.success('Product updated successfully', {position: "bottom-center", autoClose: 2000});
            props.setProduct(null);
            navigate('/');
        }catch(err){
            toast.error(err, {position: "bottom-center", autoClose: 2000});
        }
    };

    const handleChange = (event)=>{
        setEan(event.target.value);
        let matchedProducts = allProduct.filter((item) => item.ean.toLowerCase().includes(event.target.value));
        setMatchingProduct(matchedProducts);
        if(matchedProducts.length===1 && event.target.value===matchedProducts[0].ean){
            setBrand(matchedProducts[0].brand);
            setName(matchedProducts[0].name);
        }else{
            setBrand("");
            setName("");
            props.setProduct(null);
        }
    }



    return (
        <Container className="pt-3">
            <Container className="container-wrapper">
                <Form className='p-5' onSubmit={handleSubmit}>
                    <h3>Update Product</h3>
                    <Form.Group controlId="ean">
                        <Form.Label>Ean</Form.Label>
                        <Form.Control type="text" disabled value={ean} onChange={handleChange} list="matching-ean"
                        />
                        <datalist id="matching-ean">
                            {matchingProduct.map((product) => (<option key={product.ean} value={product.ean} />))}
                        </datalist>
                    </Form.Group>
                    <Form.Group controlId="name">
                        <Form.Label>Name</Form.Label>
                        <Form.Control type='text' value={name} onChange={(event) => setName(event.target.value)}/>
                    </Form.Group>
                    <Form.Group controlId="brand">
                        <Form.Label>Brand</Form.Label>
                        <Form.Control type='text' value={brand} onChange={(event) => setBrand(event.target.value)}/>
                    </Form.Group>
                    <Button variant="primary" className="w-100" type="submit">Update</Button>
                </Form>
            </Container>
        </Container>

    );
}

export default UpdateProductForm;