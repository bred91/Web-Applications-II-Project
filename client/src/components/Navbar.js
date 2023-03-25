import React from "react";
import { Link } from "react-router-dom";
function Navbar() {
    return (
        <nav className="navbar navbar-light bg-light">
            <Link to={"/"} style={{ textDecoration: "none" }}><span className="navbar-brand mb-0 h1">Ticketing Platform</span></Link>
            <Link to="/signup">
                <button className="btn btn-primary"> Signup </button>
            </Link>
        </nav>
    );
}

export default Navbar;
