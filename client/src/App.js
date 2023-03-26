import './App.css';
import 'bootstrap/dist/css/bootstrap.min.css';
import Navbar from "./components/Navbar";
import Search from "./components/Search";
import SignupForm from "./components/SignupForm";
import { BrowserRouter, Routes, Route } from "react-router-dom";
import { ToastContainer } from 'react-toastify';

function App() {
  return (
    <div className="App">
        <BrowserRouter>
            <Navbar />
            <ToastContainer />
            <Routes>
                <Route path="/" element={<Search />} />
                <Route path="/signup" element={<SignupForm />} />
            </Routes>
        </BrowserRouter>
    </div>
  );
}

export default App;
