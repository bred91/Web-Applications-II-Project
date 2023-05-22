package it.polito.server.profiles
import io.micrometer.observation.annotation.Observed
import it.polito.server.products.PurchaseDTO
import it.polito.server.tickets.TicketDTO
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@Observed
class ProfileController(private val profileService: IProfileService, private val addressRepository: IAddressRepository) {



    @GetMapping("/API/profiles")
    fun getProfiles():List<ProfileDTO> {
        return profileService.getProfiles()
    }
    @GetMapping("/API/profiles/{email}")
    fun getProfile(@PathVariable email:String):ProfileDTO? {
        return profileService.getProfileByEmail(email)
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

    @GetMapping("/API/profiles/{email}/tickets")
    fun getMyTickets(@PathVariable email: String): List<TicketDTO>{
        return profileService.getTickets(email)
    }


}


