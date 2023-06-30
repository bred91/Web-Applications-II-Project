import React from "react";
import { Card, ListGroup } from "react-bootstrap";
import bg from '../images/background.jpg'

function Search() {
    return (
        <div
            style={{
                backgroundImage: `linear-gradient(rgba(255, 255, 255, 0.5), rgba(255, 255, 255, 0.5)), url(${bg})`,
                backgroundSize: "cover",
                height: "100vh",
                display: "flex",
                justifyContent: "center",
                alignItems: "center",

            }}
        >
            <div style={{ textAlign: "center" }}>
                <h1
                    style={{
                        fontSize: "32px",
                        fontWeight: "bold",
                        color: "navy",
                        textShadow: "2px 2px 4px rgba(0, 0, 0, 0.3)",
                    }}
                >
                    Welcome to the
                </h1>
                <h2
                    style={{
                        fontSize: "60px",
                        fontWeight: "bold",
                        color: "navy",
                        textShadow: "2px 2px 4px rgba(0, 0, 0, 0.3)",
                    }}
                >
                    Ticketing platform
                    <br/>
                    <span style={{ color: "darkred" }}>G</span>
                    <span style={{ color: "green" }}>1</span>
                    <span style={{ color: "green" }}>5</span>
                </h2>
            </div>
        </div>
    );
}



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



export default Search;
