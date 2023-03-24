data class ProfileDTO(
    var email:String,
    var username:String,
    var name:String,
    var surname:String
)

fun Profile.toDTO():ProfileDTO {
    return ProfileDTO(email, username, name, surname)
}

fun ProfileDTO.toProfile():Profile{
    return Profile()
}