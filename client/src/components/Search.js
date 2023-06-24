import React, { useState, useEffect } from "react";
import { Card, ListGroup } from "react-bootstrap";

function Search() {
    return(
        <>
            <br/>
            <br/>
            <h1>Welcome to the Ticketing platform</h1>
        </>
    )
    /*I
    const [searchTerm, setSearchTerm] = useState("");
    const [allItems, setAllItems] = useState([]);
    const [matchedItems, setMatchedItems] = useState([]);

    useEffect(() => {
        fetch("/API/profiles")
            .then((response) => response.json())
            .then((data) => {
                setAllItems(data);
                //setMatchedItems(data);
            });
    }, []);

    const handleInputChange = (event) => {
        const term = event.target.value.toLowerCase();
        setSearchTerm(term);
        let matched;
        if (term === "") {
            matched = [];
        } else {
            console.log(allItems)
            matched = allItems.filter((item) =>
                item.email.toLowerCase().includes(term)
            );
        }

        setMatchedItems(matched);
    };


    return (
        <div>
            <br/>
            <input
                type="text"
                placeholder="Search items..."
                value={searchTerm}
                onChange={handleInputChange}
            />

            <ListGroup style={{ marginTop: "10px" }}>
                {matchedItems.map((item) => (
                    <Card key={item.id}>
                        <Card.Body>
                            <Card.Title>{item.email}</Card.Title>
                            <Card.Subtitle>{item.username}</Card.Subtitle>
                        </Card.Body>
                    </Card>
                ))}
            </ListGroup>
        </div>
    );
     */
}


export default Search;
