const updateProfile = async(email, username, name, surname) => {
    const res = await fetch( "/API/profiles/"+email, {
        method: "PUT",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify({ email, username, name, surname }),
    });

    if(!res.ok){
        const errorMessage = await res.json();
        console.log(errorMessage);
        throw errorMessage.detail;
    }
    else return null;

}

const getProfiles = async() => {
    const res = await fetch('/API/profiles');
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

const getProfileByEmail = async(email) => {
    const res = await fetch('/API/profiles/'+email);
    const profile = await res.json();
    if(res.ok){
        return profile;
    }else{
        console.log(profile);
        throw profile.detail;
    }
}

// Products API
const getProduct = async(token) => {
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

export{
    updateProfile, getProfiles, createProfile, getProfileByEmail,
    getProduct, getProductByEan, createProduct, updateProduct,
    login, logout, getTickets, createExpert
}