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

const createProfile = async(email, username, name, surname) => {
    const res = await fetch( "/API/profiles/", {
        method: "POST",
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

const logout = async(username) => {
    const res = await fetch( "/logout", {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify({ username }),
    });

    if(!res.ok){
        const errorMessage = await res.json();
        console.log(errorMessage);
        throw errorMessage.detail;
    }
    else return true;
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
const getProduct = async() => {
    const res = await fetch('/API/products');
    const allProducts = await res.json();
    if(res.ok){
        return allProducts;
    }else{
        throw allProducts;
    }
}

const getProductByEan = async(ean) => {
    const res = await fetch('/API/products/'+ ean);
    const product = await res.json();
    if(res.ok){
        return product;
    }else{
        console.log(product);
        throw product.detail;
    }
}

const createProduct = async(ean, name, brand) => {
    const res = await fetch( "/API/products", {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
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

const updateProduct = async(ean, name, brand) => {
    const res = await fetch( "/API/products/"+ean, {
        method: "PUT",
        headers: {
            "Content-Type": "application/json",
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
    login, logout, getTickets
}