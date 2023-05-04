package it.polito.server.profiles
import it.polito.server.Exception.NotFoundException
import it.polito.server.products.IPurchaseRepository
import it.polito.server.products.PurchaseDTO
import it.polito.server.products.toDTO
import it.polito.server.profiles.exception.DuplicateProfileException
import it.polito.server.profiles.exception.ProfileNotFoundException
import it.polito.server.tickets.ITicketRepository
import it.polito.server.tickets.TicketDTO
import it.polito.server.tickets.toDTO
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional


@Service
class ProfileService(private val profileRepository: IProfileRepository,
                     private val addressRepository: IAddressRepository,
                     private val purchaseRepository: IPurchaseRepository,
                     private val ticketRepository: ITicketRepository)
    :IProfileService {


    override fun getProfiles(): List<ProfileDTO> {
        return profileRepository.findAll().map { it.toDTO() }
    }

    override fun getProfileByEmail(email: String): ProfileDTO? {
        return  profileRepository.findByIdOrNull(email)?.toDTO()
            ?: throw ProfileNotFoundException("Profile with email $email not found")
        //profile.addresses = getAddresses(email)
        //return profile
    }

    @Transactional
    override fun createProfile(profile: ProfileDTO) {
        if(profileRepository.existsById(profile.email)) {
            throw DuplicateProfileException("Profile with email ${profile.email} already exists!")
        }

        profileRepository.save(profile.toEntity())
    }

    @Transactional
    override fun updateProfile(email:String, profile: ProfileDTO): ProfileDTO? {
        return when (profileRepository.findByIdOrNull(email)?.toDTO()) {
            null -> {throw ProfileNotFoundException("Profile with email $email not found")}
            else -> {
                profileRepository.save(profile.toEntity()).toDTO()
            }
        }
    }

    override fun getAddresses(email: String) : List<AddressDTO>{
        if(!profileRepository.existsById(email)) {
            throw ProfileNotFoundException("Profile with email ${email} does not exist")
        }
        return addressRepository.findByProfileEmail(email).map { it.toDTO()}
    }

    @Transactional
    override fun createAddress(email: String, address: AddressDTO) {
        val profile = profileRepository.findByIdOrNull(email)
            ?: throw NotFoundException("Profile with email $email not found")
        var addressEntity = address.toEntity()
        addressEntity.profile = profile
        addressRepository.save(addressEntity)

    }

    @Transactional
    override fun updateAddress(email: String, addressId: Long, address: AddressDTO): AddressDTO? {
        profileRepository.findById(email).orElseThrow {throw ProfileNotFoundException("Profile with email $email doesn't exist.")}

        val old_address = addressRepository.findById(addressId).orElseThrow {throw NotFoundException("Address id $addressId doesn't exist")}
        old_address.streetAddress=address.streetAddress
        old_address.number=address.number
        old_address.additionalInfo=address.additionalInfo
        old_address.city=address.city
        old_address.region=address.region
        old_address.state=address.state
        old_address.zip=address.zip


        return addressRepository.save(old_address).toDTO()
    }


    @Transactional
    override fun deleteAddress(email: String, addressId: Long) {
        profileRepository.findById(email).orElseThrow {throw ProfileNotFoundException("Profile with email $email doesn't exist.")}
        addressRepository.findById(addressId).orElseThrow {throw NotFoundException("Address id $addressId doesn't exist")}
        addressRepository.deleteById(addressId)
    }

    override fun getPurchases(email: String): List<PurchaseDTO> {
        if(!profileRepository.existsById(email)) {
            throw ProfileNotFoundException("Profile with email ${email} does not exist")
        }
        return purchaseRepository.findPurchasesByCustomerEmail(email).map { it.toDTO() }
    }

    override fun getTickets(email: String): List<TicketDTO> {
        if(!profileRepository.existsById(email)) {
            throw ProfileNotFoundException("Profile with email ${email} does not exist")
        }
        return ticketRepository.findByCustomerEmail(email).map { it.toDTO() }

    }


}