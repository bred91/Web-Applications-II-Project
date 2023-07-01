import React, {useEffect, useState} from "react";
import { Button, Container, Form } from "react-bootstrap";
import './SignUpForm.css';
import {toast} from 'react-toastify';
import 'react-toastify/dist/ReactToastify.min.css';
import {createProduct, createTicket, getProducts, getProductByEan, verifyPurchase} from './../API';
import { useNavigate } from "react-router-dom";
import CsvDownloadButton from "react-json-to-csv";
import * as API from "../API";

function CreateTicket(props) {
    const [isVerified, setIsVerified] = useState(false)
    const [purchase, setPurchase] = useState({})
    const navigate = useNavigate();

    return(
        <>
            {isVerified ?
                <CreateTicketComponent token={props.token} setTicketId={props.setTicketId} setTickets={props.setTickets} purchase={purchase} navigate={navigate}/>
            :
                <VerifyPurchaseComponent token={props.token} setIsVerified={setIsVerified} setPurchase={setPurchase}/>
            }
        </>
    );
}

function VerifyPurchaseComponent(props){
    const [allProducts, setAllProducts] = useState([]);
    const [name, setName] = useState("");
    const [matchingProducts, setMatchingProducts] = useState([]);
    const [warrantyCode, setWarrantyCode] = useState("")

    useEffect(() => {
        const fetchProducts = async() => {
            try{
                const products = await getProducts(props.token);
                setAllProducts(products);
                setMatchingProducts(products);
            }catch(err){
                console.log(err);
            }
        };
        fetchProducts();

    }, []);

    const handleChange = (event)=>{
        setName(event.target.value);
        let matchedProducts = allProducts.filter((item) => item.name.toLowerCase().includes(event.target.value.toLowerCase()));
        setMatchingProducts(matchedProducts);
    }

    const handleVerification = async (event) => {
        event.preventDefault();
        if(name === ""){
            toast("Please insert a product name", {position: "top-center", autoClose: 2000})
            return
        }
        if(warrantyCode === ""){
            toast("Please insert a warranty code", {position: "top-center", autoClose: 2000})
            return
        }
        let product = allProducts.find((item) => item.name.toLowerCase() === name.toLowerCase())
        if(product === null){
            toast("Invalid product name", {position: "top-center", autoClose: 2000})
            return
        }

        try{
            const purchase = await verifyPurchase(props.token, product.ean, warrantyCode)
            toast.success('Warranty verified successfully', {position: "bottom-center", autoClose: 2000});
            props.setIsVerified(true)
            props.setPurchase(purchase)
        }catch(err){
            toast.error("Invalid purchase info", {position: "bottom-center", autoClose: 2000});
        }
    };

    return (
        <Container className="pt-3">
            <Container className="container-wrapper">
                <center>
                    <Form className='p-5' onSubmit={handleVerification}>
                        <h3>Insert purchase info</h3>
                        <Form.Group controlId="name">
                            <Form.Label>Product name</Form.Label>
                            <Form.Control type="text" value={name} onChange={handleChange} list="matching-names"
                            />
                            <datalist id="matching-names">
                                {matchingProducts.map((product) => (<option key={product.name} value={product.name} />))}
                            </datalist>
                        </Form.Group>
                        <Form.Group controlId="warrantyCode">
                            <Form.Label>Warranty code</Form.Label>
                            <Form.Control type="text" value={warrantyCode} onChange={(event) => setWarrantyCode(event.target.value)}/>
                        </Form.Group>
                        <Button variant="success" className="w-100" type="submit">Verify purchase</Button>
                    </Form>
                </center>
            </Container>
        </Container>

    );
}

function CreateTicketComponent(props){
    const [note, setNote] = useState("");
    const navigate = useNavigate();
    const handleSubmit = async (event) => {
        event.preventDefault();

        try{
            const res = await createTicket(props.token, props.purchase.id)
            toast.success('Ticket submitted successfully', {position: "bottom-center", autoClose: 2000});
            props.setTickets((prevTickets)=>[...prevTickets, res]);
            const formData = new FormData()
            if(note) {
                formData.append('text', note)
            }
            await API.createMessage(props.token, res.id, formData)
            props.setTicketId(res.id);
            navigate("/tickets/" + res.id);

        }catch(err){
            toast.error(err, {position: "bottom-center", autoClose: 2000});
        }
    };

    return (
        <Container className="pt-3">
            <Container className="container-wrapper">
                <center>
                    <Form className='p-5' onSubmit={handleSubmit}>
                        <h3>Ticket info</h3>
                        <Form.Group controlId="name">
                            <Form.Label>Product name</Form.Label>
                            <Form.Control type="text" value={props.purchase.product.name} disabled/>
                        </Form.Group>
                        <Form.Group controlId="warrantyCode">
                            <Form.Label>Warranty code</Form.Label>
                            <Form.Control type="text" value={props.purchase.warrantyCode} disabled/>
                        </Form.Group>
                        <Form.Group controlId="note">
                            <Form.Label>Additional note</Form.Label>
                            <Form.Control as="textarea" value={note} onChange={(event) => setNote(event.target.value)}/>
                        </Form.Group>
                        <Button variant="success" className="w-100" type="submit">Submit ticket</Button>
                    </Form>
                </center>
            </Container>
        </Container>
    )
}

export default CreateTicket;