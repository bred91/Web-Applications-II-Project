interface IProfileService {


    fun getProfile(email:String):ProfileDTO?

    fun createProfile(profile:ProfileDTO):Boolean

    fun updateProfile(email:String, profile:ProfileDTO):Any?

}