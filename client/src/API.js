const SERVER_URL = 'http://localhost:3003';

const updateProfile = async(email, username, name, surname) => {
    const res = await fetch(SERVER_URL+ "/api/profiles/"+email, {
            method: "PUT",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify({ email, username, name, surname }),
        });
    
    if(!res.ok){
        const erroMessage = await res.json();
        console.log(erroMessage);
        throw erroMessage.detail;
    }
    else return null;

}

const getProfiles = async() => {
    const res = await fetch(SERVER_URL+'/api/profiles');
     const allProfiles = await res.json();
     if(res.ok){
        return allProfiles;
     }else{
        throw allProfiles;
     }
}

const createProfile = async(email, username, name, surname) => {
    const res = await fetch(SERVER_URL+ "/api/profiles/", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify({ email, username, name, surname }),
        });
    
    if(!res.ok){
        const erroMessage = await res.json();
        console.log(erroMessage);
        throw erroMessage.detail;
    }
    else return null;

}

const getProfileByEmail = async(email) => {
    const res = await fetch(SERVER_URL+'/api/profiles/'+email);
     const profile = await res.json();
     if(res.ok){
        return profile;
     }else{
        console.log(profile);
        throw profile.detail;
     }
}

export{updateProfile, getProfiles, createProfile, getProfileByEmail}