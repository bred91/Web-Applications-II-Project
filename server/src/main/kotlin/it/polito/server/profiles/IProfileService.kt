package it.polito.server.profiles
interface IProfileService {

    fun getProfiles():List<ProfileDTO>
    
    fun getProfile(email:String):ProfileDTO?

    fun createProfile(profile:ProfileDTO)

    fun updateProfile(email:String, profile:ProfileDTO):ProfileDTO?

}