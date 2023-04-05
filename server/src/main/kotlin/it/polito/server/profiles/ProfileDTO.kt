package it.polito.server.profiles


import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank


data class ProfileDTO(

    @field:Email(message="Not a valid email address")
    var email:String,
    @field:NotBlank(message="Not a valid username")
    var username:String,
    @field:NotBlank(message="Not a valid name")
    var name:String,
    @field:NotBlank(message="Not a valid surname")
    var surname:String


)

fun Profile.toDTO():ProfileDTO {
    return ProfileDTO(email, username, name, surname)
}

fun ProfileDTO.toProfile():Profile{
    val profile = Profile()
    profile.email = email
    profile.name = name
    profile.surname = surname
    profile.username = username
    return profile
}