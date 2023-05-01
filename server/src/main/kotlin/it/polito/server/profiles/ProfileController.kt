package it.polito.server.profiles
import it.polito.server.products.PurchaseDTO
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
class ProfileController(private val profileService: IProfileService, private val addressRepository: IAddressRepository) {



    @GetMapping("/API/profiles")
    fun getProfiles():List<ProfileDTO> {
        return profileService.getProfiles()
    }
    @GetMapping("/API/profiles/{email}")
    fun getProfile(@PathVariable email:String):ProfileDTO? {
        return profileService.getProfile(email)
    }


    @GetMapping("/API/profiles/{email}/addresses")
    fun getAddressesByProfile(@PathVariable email: String) : List<AddressDTO> {
        //return addressRepository.findByProfileEmail(email).map { it.toDTO()}
        return profileService.getAddresses(email)
    }

    @GetMapping("/API/profiles/{email}/purchases")
    fun getPurchasesByProfile(@PathVariable email: String) : List<PurchaseDTO>{
        return profileService.getPurchases(email)
    }


    @PostMapping("/API/profiles/")
    @ResponseStatus(HttpStatus.CREATED)
    fun createProfile(@Valid @RequestBody profile: ProfileDTO) {
        profileService.createProfile(profile)
    }

    @PostMapping("/API/profiles/{email}/address")
    @ResponseStatus(HttpStatus.CREATED)
    fun createAddress(@PathVariable email: String, @Valid @RequestBody address: AddressDTO ){
        profileService.createAddress(email, address)
    }

    @PutMapping("/API/profiles/{email}")
    fun updateProfile(@PathVariable email:String, @Valid @RequestBody profile:ProfileDTO):ProfileDTO? {

        return profileService.updateProfile(email, profile)
    }

    @PutMapping("/API/profiles/{email}/addresses/{addressId}")
    fun updateAddress(@PathVariable email: String, @PathVariable addressId: Long, @Valid @RequestBody address: AddressDTO) : AddressDTO?{
        return profileService.updateAddress(email, addressId, address)
    }


    @DeleteMapping("/API/profiles/{email}/addresses/{addressId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteAddress(@PathVariable email: String, @PathVariable addressId: Long) {
        profileService.deleteAddress(email, addressId)
    }


}


