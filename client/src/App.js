import './App.css';
import 'bootstrap/dist/css/bootstrap.min.css';
import TSNavbar from "./components/Navbar";
import Search from "./components/Search";
import SignupForm from "./components/SignupForm";
import SearchProfile from './components/SearchProfile';
import UpdateProfileForm from './components/UpdateProfileForm';
import SearchProduct from './components/SearchProduct';
import {BrowserRouter, Routes, Route} from "react-router-dom";
import { ToastContainer } from 'react-toastify';
import { useEffect, useState} from 'react';
import CreateProduct from "./components/CreateProduct";
import UpdateProductForm from "./components/UpdateProductForm";
import LoginForm from "./components/LoginForm";
import Tickets from "./components/Tickets";
import CreateExpert from "./components/CreateExpert";
import CreateTicket from "./components/CreateTicket"
import NotFoundRoute from "./components/NotFound";
import { toast } from "react-toastify";
import Ticket from "./components/Ticket";
import SockJS from "sockjs-client";
import {Stomp} from "@stomp/stompjs";
import * as API from "./API";
import Monitoring from "./components/Monitoring";


var stompClient =null;
function App() {
    const [isLoggedIn, setIsLoggedIn] = useState(false);
    const [user, setUser] = useState(null);
    const [accessToken, setAccessToken] = useState(null);
    const [refreshToken, setRefreshToken] = useState(null);
    const [role, setRole] = useState(null);

    const [profile, setProfile] = useState(null);
    const [product, setProduct] = useState(null);

    const [subscriptions, setSubscriptions] = useState(new Map());
    const [messages, setMessages] = useState([]);
    const [ticketId, setTicketId] = useState(null);
    const [ticketDetails, setTicketDetails] = useState(null);
    const [tickets, setTickets] = useState([]);
    const [loading, setLoading] = useState(false);



    useEffect(() => {
        if(isLoggedIn){
            setLoading(true);
            let Sock = new SockJS('http://localhost:8080/ws');
            stompClient = Stomp.over(Sock);
            stompClient.connect({},onConnected, onError);
            API.getTickets(accessToken)
                .then((tick) => {
                    setTickets(tick.sort((a,b)=>a.id>b.id));
                    setLoading(false);
                })
                .catch((err) => {
                    toast.error(err.message);
                    setLoading(false);
                });

            // Clean up the WebSocket connection
            return () => {
                disconnect();
            };
        }else{
            setTickets([]);
            setMessages([]);
            setTicketId(null);
            setTicketDetails(null);
        }
    }, [isLoggedIn]);

    useEffect(() =>{
        if(ticketId){
            const messagesSubscription = stompClient.subscribe('/topics/'+ticketId+'/messages', onReceivedMessage);
            const ticketSubscription = stompClient.subscribe('/topics/'+ticketId+'/ticket', onUpdateTicket);
            const newSubscriptions = new Map(subscriptions);
            newSubscriptions.set('messages', messagesSubscription);
            newSubscriptions.set('ticket', ticketSubscription);
            setSubscriptions(newSubscriptions);

        }else{
            const newSubscriptions = new Map(subscriptions);
            if(newSubscriptions.get('messages')){
                newSubscriptions.get('messages').unsubscribe()
                newSubscriptions.delete('messages');
            }
            if(newSubscriptions.get('ticket')){
                newSubscriptions.get('ticket').unsubscribe()
                newSubscriptions.delete('ticket');
            }
            setSubscriptions(newSubscriptions);
            setTicketId(null);
            setMessages([]);
            setTicketDetails(null);
        }
    },[ticketId])

    const onConnected = () => {
        let ticketsSubscription
        if(role==='Manager'){
            ticketsSubscription = stompClient.subscribe('/topics/'+'manager'+'/tickets', onReceivedTicket);
        }else{
            ticketsSubscription = stompClient.subscribe('/topics/'+user.email+'/tickets', onReceivedTicket);
        }

        const newSubscriptions = new Map(subscriptions);
        newSubscriptions.set('tickets', ticketsSubscription);
        setSubscriptions(newSubscriptions);

    };

    const onUpdateTicket = (updatedTicket) => {
        const receivedMessage = JSON.parse(updatedTicket.body);
        setTicketDetails(receivedMessage);
    }
    const onReceivedMessage = (message) => {
        const receivedMessage = JSON.parse(message.body);
        setMessages((prevMessages) => [...prevMessages, receivedMessage]);
    }
    const onReceivedTicket = (ticket) => {
        const receivedTicket = JSON.parse(ticket.body);
        if(role==='Expert' && (receivedTicket.actualExpert==null || receivedTicket.actualExpert.email != user.email) ){
            setTicketId(null);
            setTickets((prevTickets) => {
                return prevTickets.filter((t)=>t.id!=receivedTicket.id);
            });

        }else{
            setTickets((prevTickets) => {
                const updatedArray = [...prevTickets];
                const index = updatedArray.findIndex(obj => obj.id == receivedTicket.id);
                if(index !== -1){
                    updatedArray[index] = receivedTicket;
                }else{
                    updatedArray.push(receivedTicket)
                }
                return updatedArray
            });
        }


    }

    const onError = (error) => {
        console.error('Error during WebSocket connection:', error);
    }

    const disconnect = () => {
        setTickets([]);
        setTicketId(null);
        if (stompClient) {
            unsubscribe()
            stompClient.disconnect();
        }
    };

    const unsubscribe = () => {

        subscriptions.forEach((subscription) => {
            subscription.unsubscribe();
        });
        setSubscriptions(new Map());
    };

    return (
        <div className="App">
            <BrowserRouter>
                <TSNavbar isLoggedIn={isLoggedIn} setTicketId={setTicketId} setIsLoggedIn={setIsLoggedIn} user={user} setUser={setUser} setRole={setRole} accessToken={accessToken} refreshToken={refreshToken} role={role} setAccessToken={setAccessToken} setRefreshToken={setRefreshToken}/>
                <ToastContainer />
                <Routes>
                    <Route path="/" element={<Search />} />
                    <Route path="/signup" element={<SignupForm />} />
                    <Route path="/login" element={<LoginForm setIsLoggedIn={setIsLoggedIn} setUser={setUser} setAccessToken={setAccessToken} setRefreshToken={setRefreshToken} setRole={setRole} setProfile={setProfile}/>} />
                    {isLoggedIn && role==='Client' ? <Route path="/updateProfile" element={<UpdateProfileForm token={accessToken} profile={profile} setProfile={setProfile} user={user}/>} /> : null}
                    {isLoggedIn && role==='Manager' ? <Route path="/searchProfile" element={<SearchProfile token={accessToken} setProfile={setProfile}/>} /> : null}
                    {isLoggedIn && role!=='Client' ? <Route path="/searchProduct" element={<SearchProduct token={accessToken} setProduct={setProduct} role={role}/>} /> : null}
                    {isLoggedIn && role==='Manager' ? <Route path="/createExpert" element={<CreateExpert token={accessToken} />} /> : null}
                    {isLoggedIn && role==='Manager' ? <Route path="/createProduct" element={<CreateProduct token={accessToken} />} /> : null}
                    {isLoggedIn && role==='Manager' ? <Route path="/updateProduct" element={<UpdateProductForm token={accessToken} product={product} setProduct={setProduct}/>} /> : null}
                    {isLoggedIn && role==='Manager' ? <Route path="/monitoring" element={<Monitoring accessToken={accessToken} user={user}/>} /> : null}
                    {isLoggedIn ? <Route path="/tickets" element={<Tickets accessToken={accessToken} tickets={tickets} loading={loading} setTicketId={setTicketId} setTickets={setTickets} role={role}/>}/> : null}
                    {isLoggedIn ? <Route path="/tickets/:ticketId" element={<Ticket accessToken={accessToken} user={user}  ticketID={ticketId} loading={loading} role={role} messages={messages} ticketDetails={ticketDetails} setTicketDetails={setTicketDetails} setTickets={setTickets} setMessages={setMessages}/>}/> : null}
                    {isLoggedIn && role==='Client' ? <Route path="/createTicket" element={<CreateTicket token={accessToken} setTicketId={setTicketId} setTickets={setTickets}/>} /> : null}
                    <Route path='*' element={<NotFoundRoute />} />
                </Routes>
            </BrowserRouter>
        </div>
    );
}

export default App;