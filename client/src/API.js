const updateProfile = async(token, email, username, name, surname, phoneNumber) => {
    const res = await fetch( "/API/profiles/"+email, {
        method: "PUT",
        headers: {
            "Content-Type": "application/json",
            "Authorization": `Bearer ${token}`
        },
        body: JSON.stringify({ email, username, name, surname, phoneNumber }),
    });

    if(!res.ok){
        const errorMessage = await res.json();
        console.log(errorMessage);
        throw errorMessage.detail;
    }
    else return null;

}

const getProfiles = async(token) => {
    const res = await fetch('/API/profiles',{
            method: "GET",
            headers: {
                "Authorization": `Bearer ${token}`
            }
        }
    );
    const allProfiles = await res.json();
    if(res.ok){
        return allProfiles;
    }else{
        throw allProfiles;
    }
}

const createProfile = async(email, password, username, name, surname, phoneNumber) => {
    const res = await fetch( "/signup", {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify({ email, password, username, name, surname, phoneNumber }),
    });

    if(!res.ok){
        const errorMessage = await res.json();
        console.log(errorMessage);
        throw errorMessage.detail;
    }
    else return null;

}

const login = async(username, password) => {
    const res = await fetch( "/login", {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify({ username, password }),
    });

    if(!res.ok){
        const errorMessage = await res.json();
        console.log(errorMessage);
        throw errorMessage.detail;
    }
    else return res.json();
}

const logout = async(accessToken, refreshToken) => {
    const res = await fetch( "/logout2", {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify({ accessToken, refreshToken }),
    });

    if(!res.ok){
        const errorMessage = await res.json();
        console.log(errorMessage);
        throw errorMessage.detail;
    }
    else return res;
}

const getProfileByEmail = async(token, email) => {
    const res = await fetch('/API/profiles/'+email,
        {
            method: "GET",
            headers: {
                "Authorization": `Bearer ${token}`
            }
        });
    const profile = await res.json();
    if(res.ok){
        return profile;
    }else{
        console.log(profile);
        throw profile.detail;
    }
}

const getPerformance = async(token) => {
    const res = await fetch('/API/performance',{
            method: "GET",
            headers: {
                "Authorization": `Bearer ${token}`
            }
    });

    const performances = await res.json();
    if(res.ok){
        return performances;
    }else{
        throw performances;
    }
}

const getExpertsPerformance = async(token) => {
    const res = await fetch('/API/performance/experts',{
        method: "GET",
        headers: {
            "Authorization": `Bearer ${token}`
        }
    });

    const employees = await res.json();
    if(res.ok){
        return employees;
    }else{
        throw employees;
    }
}

// Products API
const getProducts = async(token) => {
    const res = await fetch('/API/products', {
        method: "GET",
        headers: {
            "Authorization": `Bearer ${token}`
        }
    });
    const allProducts = await res.json();
    if(res.ok){
        return allProducts;
    }else{
        throw allProducts;
    }
}

const getProductByEan = async(token, ean) => {
    const res = await fetch('/API/products/'+ ean, {
        method: "GET",
        headers: {
            "Authorization": `Bearer ${token}`
        }
    });
    const product = await res.json();
    if(res.ok){
        return product;
    }else{
        console.log(product);
        throw product.detail;
    }
}

const createProduct = async(token, ean, name, brand) => {
    const res = await fetch( "/API/products", {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            "Authorization": `Bearer ${token}`
        },
        body: JSON.stringify({ ean, name, brand }),
    });

    if(!res.ok){
        const errorMessage = await res.json();
        console.log(errorMessage);
        throw errorMessage.detail;
    }
    else return null;
}

const updateProduct = async(token, ean, name, brand) => {
    const res = await fetch( "/API/products/"+ean, {
        method: "PUT",
        headers: {
            "Content-Type": "application/json",
            "Authorization": `Bearer ${token}`
        },
        body: JSON.stringify({ ean, name, brand }),
    });

    if(!res.ok){
        const errorMessage = await res.json();
        console.log(errorMessage);
        throw errorMessage.detail;
    }
    else return null;
}

const createExpert = async(email, username, firstName, lastName, password, token) => {
    const res = await fetch( "/createExpert", {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            "Authorization": `Bearer ${token}`
        },
        body: JSON.stringify({ username, email, lastName, firstName, password }),
    });

    if(!res.ok){
        const errorMessage = await res.json();
        console.log(errorMessage);
        throw errorMessage.detail;
    }
    else return null;
}

const getTickets = async(token) => {
    const res = await fetch('/API/tickets', {
        method: "GET",
        headers: {
            "Authorization": `Bearer ${token}`
        }
    })

    const ticketsResponse = await res.json();
    if(res.ok){
        return ticketsResponse;
    }else{
        throw ticketsResponse.detail;
    }
}

const createTicket = async(token, purchaseId) => {
    const res = await fetch( `/API/tickets/createIssue?purchaseId=${purchaseId}`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            "Authorization": `Bearer ${token}`
        }
    });

    const ticketResponse = await res.json();
    if(res.ok){
        return ticketResponse;
    }else{
        console.log(ticketResponse);
        throw ticketResponse.detail;
    }
}

const verifyPurchase = async(token, ean, warrantyCode) => {
    const res = await fetch( "/API/purchases/verify", {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            "Authorization": `Bearer ${token}`
        },
        body: JSON.stringify({ean, warrantyCode} ),
    });

    const verifyResponse = await res.json();
    if(res.ok){
        return verifyResponse;
    }else{
        console.log(verifyResponse);
        throw verifyResponse.detail;
    }
}


export{
    updateProfile, getProfiles, createProfile, getProfileByEmail, getExpertsPerformance,
    getProducts, getProductByEan, createProduct, updateProduct, getPerformance,
    login, logout, getTickets, createExpert, createTicket, verifyPurchase
}