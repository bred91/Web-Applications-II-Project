package it.polito.server.profiles
import it.polito.server.Exception.NotFoundException
import it.polito.server.profiles.exception.DuplicateProfileException
import it.polito.server.profiles.exception.ProfileNotFoundException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.PathVariable


@Service
class ProfileService(private val profileRepository: IProfileRepository,
                     private val addressRepository: IAddressRepository )
    :IProfileService {


    override fun getProfiles(): List<ProfileDTO> {
        return profileRepository.findAll().map { it.toDTO() }
    }

    override fun getProfile(email: String): ProfileDTO? {
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


}