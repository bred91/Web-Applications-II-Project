import './App.css';
import 'bootstrap/dist/css/bootstrap.min.css';
import TSNavbar from "./components/Navbar";
import Search from "./components/Search";
import SignupForm from "./components/SignupForm";
import SearchProfile from './components/SearchProfile';
import UpdateProfileForm from './components/UpdateProfileForm';
import SearchProduct from './components/SearchProduct';
import { BrowserRouter, Routes, Route } from "react-router-dom";
import { ToastContainer } from 'react-toastify';
import {createContext, useState} from 'react';
import CreateProduct from "./components/CreateProduct";
import UpdateProductForm from "./components/UpdateProductForm";
import LoginForm from "./components/LoginForm";
import Tickets from "./components/Tickets";
import CreateExpert from "./components/CreateExpert";
import CreateTicket from "./components/CreateTicket"
import NotFoundRoute from "./components/NotFound";
import Chat from "./components/Chat";

function App() {
    const [isLoggedIn, setIsLoggedIn] = useState(false);
    const [user, setUser] = useState(null);
    const [accessToken, setAccessToken] = useState(null);
    const [refreshToken, setRefreshToken] = useState(null);
    const [role, setRole] = useState(null);

    const [profile, setProfile] = useState(null);
    const [product, setProduct] = useState(null);

    return (
        <div className="App">
            <BrowserRouter>
                <TSNavbar isLoggedIn={isLoggedIn} setIsLoggedIn={setIsLoggedIn} user={user} setUser={setUser} setRole={setRole} accessToken={accessToken} refreshToken={refreshToken} role={role} setAccessToken={setAccessToken} setRefreshToken={setRefreshToken}/>
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
                    {isLoggedIn ? <Route path="/tickets" element={<Tickets accessToken={accessToken} role={role}/>}/> : null}
                    {isLoggedIn ? <Route path="/ticket/:ticketId" element={<Chat accessToken={accessToken} profile={profile} role={role}/>}/> : null}
                    {isLoggedIn && role==='Client' ? <Route path="/createTicket" element={<CreateTicket token={accessToken}/>} /> : null}
                    <Route path='*' element={<NotFoundRoute />} />
                </Routes>
            </BrowserRouter>
        </div>
    );
}

export default App;
