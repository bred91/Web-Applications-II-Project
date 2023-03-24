package it.polito.server.profiles

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank

data class ProfileDTO(

    @Email(message="Not a valid email address")
    var email:String,
    @NotBlank(message="Not a valid username")
    var username:String,
    @NotBlank(message="Not a valid name")
    var name:String,
    @NotBlank(message="Not a valid surname")
    var surname:String
)

fun Profile.toDTO():ProfileDTO {
    return ProfileDTO(email, username, name, surname)
}

fun ProfileDTO.toProfile():Profile{
    var profile = Profile()
    profile.email = email
    profile.name = name
    profile.surname = surname
    profile.username = username
    return profile
}