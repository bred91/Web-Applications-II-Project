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
import { useState } from 'react';
import CreateProduct from "./components/CreateProduct";
import UpdateProductForm from "./components/UpdateProductForm";
import LoginForm from "./components/LoginForm";

function App() {
    const [isLoggedIn, setIsLoggedIn] = useState(false);
    const [user, setUser] = useState(null);
    const [token, setToken] = useState(null);



    const [profile, setProfile] = useState(null);
    const [product, setProduct] = useState(null);


    return (
        <div className="App">
            <BrowserRouter>
                <TSNavbar isLoggedIn={isLoggedIn} user={user} token={token}/>
                <ToastContainer />
                <Routes>
                    <Route path="/" element={<Search />} />
                    <Route path="/signup" element={<SignupForm />} />
                    <Route path="/login" element={<LoginForm setIsLoggedIn={setIsLoggedIn} setUser={setUser} setToken={setToken}/>} />
                    <Route path="/updateProfile" element={<UpdateProfileForm profile={profile} setProfile={setProfile}/>} />
                    <Route path="/searchProfile" element={<SearchProfile setProfile={setProfile} />} />
                    <Route path="/searchProduct" element={<SearchProduct setProduct={setProduct} />} />
                    <Route path="/createProduct" element={<CreateProduct />} />
                    <Route path="/updateProduct" element={<UpdateProductForm product={product} setProduct={setProduct}/>} />
                </Routes>
            </BrowserRouter>
        </div>
    );
}

export default App;
