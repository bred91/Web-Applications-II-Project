import {useEffect, useState} from "react";
import * as API from "../API";
import {BarList, Bold, Card, Flex, Metric, Text, Title} from "@tremor/react";
import {Col, Container, Row} from "react-bootstrap";

function Monitoring(props){
    const [performance, setPerformance] = useState(null);
    const [experts, setExperts] = useState([]);
    const [selectedExpert, setSelectedExpert] = useState(0);

    useEffect(() => {
        API.getPerformance(props.accessToken)
        .then(res => {
            //console.log(res);
            setPerformance(res);
        })
        .catch(err => {
            console.log(err);
        });
    }, [props.accessToken]);

    useEffect(() => {
        API.getExpertsPerformance(props.accessToken)
        .then(res => {
            //console.log(res);
            setExperts(res);
        })
        .catch(err => {
            console.log(err);
        });
    }, [props.accessToken]);

    const handleDropdownExpert = (event) => {
        setSelectedExpert(event.target.value);
    }

    return(
        <Container className="container-wrapper">
                <h2 className="mt-4">Monitoring</h2>
                <br/>
                <Row className="mb-5 mx-auto col-6">
                    <select className="form-select" onChange={handleDropdownExpert} value={selectedExpert}>
                        {
                            [{employeeDTO: {id: 0, name: "All"}, performanceDTO: performance}, ...experts]
                                .map((exp) =>
                                    <option key={exp.employeeDTO.id} value={exp.employeeDTO.id}>{exp.employeeDTO.name}</option>
                                )
                        }
                    </select>
                </Row>
                <Row className="mb-3">
                    {performance && experts ?
                            [{employeeDTO: {id: 0, name: "All"}, performanceDTO: performance}, ...experts]
                            .filter(x => x.employeeDTO.id === parseInt(selectedExpert))[0].performanceDTO
                            .stateCount
                            .filter(x => (selectedExpert != 0 && x.state !== 'OPEN')
                                || (selectedExpert == 0 && x.state !== 'CLOSED'))
                                .map((element,idx) =>
                                <Col key={idx}>
                                    <Card className="d-flex max-w-xs mx-auto mb-2 align-items-center" decoration="top"
                                          decorationColor={element.state === 'REOPENED' ? "red"
                                              : element.state === 'OPEN' ? "yellow"
                                                  : element.state === 'IN_PROGRESS' ? "blue"
                                              : "green"} key={idx}>
                                        <Text>{element.state.replace("_"," ")}</Text>
                                        <Metric className="mx-auto">{element.count}</Metric>
                                    </Card>
                                </Col>
                            )
                        : (<p>loading...</p>)
                    }
                </Row>
                <Row className="mb-3">
                    {performance && experts ?
                            [{employeeDTO: {id: 0, name: "All"}, performanceDTO: performance}, ...experts]
                            .filter(x => x.employeeDTO.id === parseInt(selectedExpert))[0].performanceDTO
                            .percentageCounter
                            .map((element,idx) =>
                                <Col key={idx}>
                                    <Card className="d-flex max-w-xs mx-auto mb-2 align-items-center" decoration="top"
                                            decorationColor={"indigo"} key={idx}>
                                        <Text>{element.state.replace("_"," ") + " % in a week"}</Text>
                                        <Metric className="mx-auto">{element.count + " %"}</Metric>
                                    </Card>
                                </Col>
                            )
                        : null
                    }
                </Row>
                <Row className="mb-3">
                    <Col className="align-content-center mx-auto mb-3">
                        {performance && experts ?
                            <Card className="max-w-lg mx-auto">
                                <Title>Last Quarter Analysis</Title>
                                <Flex className="mt-4">
                                    <Text>
                                        <Bold>State</Bold>
                                    </Text>
                                    <Text>
                                        <Bold>Count</Bold>
                                    </Text>
                                </Flex>
                                <BarList data={
                                    performance && experts ?
                                        [{employeeDTO: {id: 0, name: "All"}, performanceDTO: performance}, ...experts]
                                            .filter(x => x.employeeDTO.id === parseInt(selectedExpert))[0].performanceDTO
                                            .ticketsCounter
                                            .map((element,idx) =>
                                                Object.create({
                                                    name: element.state.replace("tickets","").toUpperCase(),
                                                    value: element.count,
                                                    key: idx
                                                })
                                            ) : []
                                } className="mt-2" />
                            </Card>
                            : null
                        }
                    </Col>
                </Row>
            </Container>
    )
}

export default Monitoring;