interface IProfileService {


    fun getProfile(email:String):ProfileDTO?

    fun createProfile(profile:ProfileDTO):Unit

    fun updateProfile(profile:ProfileDTO):Unit

}