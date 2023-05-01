package it.polito.server.profiles

import org.springframework.web.bind.annotation.PathVariable

interface IProfileService {

    fun getProfiles():List<ProfileDTO>
    
    fun getProfile(email:String):ProfileDTO?

    fun createProfile(profile:ProfileDTO)

    fun updateProfile(email:String, profile:ProfileDTO):ProfileDTO?

    fun getAddresses(email: String) : List<AddressDTO>

    fun createAddress(email: String, address: AddressDTO)

}