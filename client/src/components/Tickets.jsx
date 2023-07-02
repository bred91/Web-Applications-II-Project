import React, { useState} from "react";
import { Container} from "react-bootstrap";
import { useNavigate } from "react-router-dom";
import { DataTable, Text, Box, Select } from "grommet";
import { FormDown } from "grommet-icons";
import { CircleSpinner } from "react-spinners-kit";




const getPriorityColor = (priority) => {
    switch (priority) {
        case "HIGH":
            return "red";
        case "MEDIUM":
            return "orange";
        case "LOW":
            return "green";
        default:
            return "black";
    }
};



const stateOptions = [
    { label: "All", value: "" },
    { label: "Open", value: "OPEN" },
    { label: "In Progress", value: "IN_PROGRESS" },
    { label: "Resolved", value: "RESOLVED" },
    { label: "Closed", value: "CLOSED" },
    { label: "Reopened", value: "REOPENED" },
];


function Tickets(props) {

    const columns = [
        {
            property: "id",
            header: <Text>ID</Text>,
            primary: true,
            visible: false,
        },
        {
            property: "product",
            header: "Product",
        },
        {
            property: "creationDate",
            header: <Text>Creation Date</Text>,
        },
        {
            property: "name",
            header: "State",
            render: (data) => (
                <Box
                    pad="small"
                    background="light-4"
                    round="small"
                    style={{ pointerEvents: "none" }}
                >
                    <Text>{data.name}</Text>
                </Box>
            ),
        },
        {
            property: "lastModification",
            header: "Last Modified",
        },
        props.role !== "Client" && {
            property: "priorityLevel",
            header: "Priority Level",
            render: (data) => (
                <Text color={getPriorityColor(data.priorityLevel)}>
                    {data.priorityLevel}
                </Text>
            ),
        },
        {
            property: "actualExpert",
            header: "Expert",
        },
        {
            property: "expiringDate",
            header: "Warranty Expiration",
        },
    ].filter(Boolean);


    //const [tickets, setTickets] = useState([]);
    const [filter, setFilter] = useState("");
    const navigate = useNavigate();

    // useEffect(() => {
    //     API.getTickets(props.accessToken)
    //         .then((tick) => {
    //             props.setTickets(tick);
    //             setLoading(false);
    //         })
    //         .catch((err) => {
    //             toast.error(err.message);
    //             setLoading(false);
    //         });
    // }, []);

    const handleClick = (id) => {
        props.setTicketId(id);
        navigate("/tickets/" + id);
    };

    const handleFilterChange = (event) => {
        setFilter(event.option.value);
    };

    const filteredTickets = filter
        ? props.tickets.filter((ticket) => ticket.state.name === filter)
        : props.tickets;

    return (
        <Container className="mt-5" >
            <Box align="end" margin={{ bottom: "medium" }}>
                <Select
                    placeholder="Filter by State"
                    value={filter}
                    options={stateOptions}
                    onChange={handleFilterChange}
                    icon={<FormDown />}
                    style={{ minWidth: "200px" }}
                />
            </Box>

            {props.loading ? (
                <Box align="center" margin={{ vertical: "medium" }}>
                    <CircleSpinner size={60} color="#5f8dd3" />
                </Box>
            ) : (
                <DataTable
                    onClickRow={({ datum }) => handleClick(datum.id)}
                    sortable={true}
                    search={true}

                    size="medium"
                    columns={columns}
                    data={filteredTickets.map((ticket) => {
                        return {
                            id: ticket.id,
                            product: ticket.purchase.product.name,
                            creationDate: ticket.creationDate.split('T')[0],
                            name: ticket.state.name.replace("_", " "),
                            lastModification: ticket.lastModification
                                .replace("T", "  ")
                                .split(".")[0]
                                .split(":")
                                .slice(0, -1)
                                .join()
                                .replace(",", ":"),
                            priorityLevel: ticket.priorityLevel
                                ? ticket.priorityLevel.name
                                : "",
                            actualExpert: ticket.actualExpert
                                ? ticket.actualExpert.name + " " + ticket.actualExpert.surname
                                : "",
                            expiringDate: ticket.purchase.expiringDate,
                        };
                    })}
                />
            )}
        </Container>
    );
}

export default Tickets;