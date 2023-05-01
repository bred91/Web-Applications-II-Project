package it.polito.server.profiles

import it.polito.server.products.PurchaseDTO
import org.springframework.web.bind.annotation.PathVariable

interface IProfileService {

    fun getProfiles():List<ProfileDTO>
    
    fun getProfile(email:String):ProfileDTO?

    fun createProfile(profile:ProfileDTO)

    fun updateProfile(email:String, profile:ProfileDTO):ProfileDTO?

    fun getAddresses(email: String) : List<AddressDTO>

    fun createAddress(email: String, address: AddressDTO)

    fun updateAddress(email: String, addressId: Long, address: AddressDTO): AddressDTO?

    fun deleteAddress(email:String, addressId:Long)

    fun getPurchases(email: String): List<PurchaseDTO>

}