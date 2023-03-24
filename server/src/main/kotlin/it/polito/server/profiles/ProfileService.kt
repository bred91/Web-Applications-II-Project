package it.polito.server.profiles
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class ProfileService(private val profileRepository: IProfileRepository):IProfileService {
    override fun getProfile(email: String): ProfileDTO? {
        println("EMAIL = $email")
        return profileRepository.findByIdOrNull(email)?.toDTO()
    }

    override fun createProfile(profile: ProfileDTO):Boolean {

        if(profile == null) return  false

        return when(profileRepository.existsById(profile.email)) {
            true -> {false}
            else -> {
                profileRepository.save(profile.toProfile())
                true
            }
        }
    }

    override fun updateProfile(email:String, profile: ProfileDTO): Any? {
        return when (profileRepository.findByIdOrNull(email)?.toDTO()) {
            null -> {null}
            else -> {
                profileRepository.save(profile.toProfile())
            }
        }
    }

}