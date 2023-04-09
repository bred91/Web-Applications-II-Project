import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { Button, Container, Form } from "react-bootstrap";
import './SignUpForm.css';
import {toast} from 'react-toastify';
import 'react-toastify/dist/ReactToastify.min.css';
import { getProduct,  getProductByEan } from '../API';
import './ShowProfile.css'
import CsvDownloadButton from 'react-json-to-csv'


function SearchProduct(props) {
    const [allProducts, setAllProducts] = useState([]);
    const [ean, setEan] = useState("");
    const [product, setProduct] = useState(null);
    const [matchingProducts, setMatchingProducts] = useState([]);
    const [showProduct, setShowProduct] = useState(false);
    const navigate = useNavigate();


    useEffect(() => {
        const fetchProducts = async() => {
            try{
                const products = await getProduct();
                setAllProducts(products);
                setMatchingProducts(products);
            }catch(err){
                console.log(err);
            }
        };
        fetchProducts();

    }, []);

    const handleSubmit = async (event) => {
        event.preventDefault();
        if (ean === "") {
            toast("Please enter a EAN", {position: "top-center"})
            return;
        }

        try{
            const product = await getProductByEan(ean);
            setProduct(product);
            setShowProduct(true);

        }catch(err){
            toast.error(err, {position: "top-center"});
        }
    };

    const handleChange = (event)=>{
        setEan(event.target.value);
        let matchedProducts = allProducts.filter((item) => item.ean.toLowerCase().includes(event.target.value));
        setMatchingProducts(matchedProducts);
        if(matchedProducts.length===1 && event.target.value===matchedProducts[0].ean){
            setProduct(matchedProducts[0]);
            setShowProduct(true);
        }else{
            setProduct(null);
            setShowProduct(false);
        }
    }

    const handleUpdateClick = async() => {
        props.setProduct(product);
        navigate('/updateProduct');
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
                        <h3>Search Product</h3>
                        <Form.Group controlId="ean">
                            <Form.Label>EAN</Form.Label>
                            <Form.Control type="text" value={ean} onChange={handleChange} list="matching-eans"
                            />
                            <datalist id="matching-eans">
                                {matchingProducts.map((product) => (<option key={product.ean} value={product.ean} />))}
                            </datalist>
                        </Form.Group>
                        <Button variant="success" className="w-100" type="submit">Search</Button>
                        <Button variant="warning" className="w-100" onClick={handleDownloadCSV}>Download all</Button>
                    </Form>
                </center>
                <CsvDownloadButton id="csv" data={allProducts} delimiter={","}
                                   filename={"AllProducts.csv"} style={{display:"none"}}/>
                { showProduct && <div>
                    <center><div className="col">
                        <button className="button rounded-corners disabled"><strong>EAN: </strong>{product.ean}</button>
                    </div></center>
                    <center><div className="col">
                        <button className="button rounded-corners disabled"><strong>Name: </strong>{product.name}</button>
                    </div></center>
                    <center><div className="col">
                        <button className="button rounded-corners disabled"><strong>Brand: </strong>{product.brand}</button>
                    </div></center>
                    <Button variant="primary" className="editButton mb-3" onClick={handleUpdateClick} >Edit</Button>
                </div>}
            </Container>
        </Container>

    );
}

export default SearchProduct;
