package it.polito.server.profiles
interface IProfileService {

    fun getProfiles():List<ProfileDTO>
    
    fun getProfile(email:String):ProfileDTO?

    fun createProfile(profile:ProfileDTO):Boolean

    fun updateProfile(email:String, profile:ProfileDTO):Any?

}