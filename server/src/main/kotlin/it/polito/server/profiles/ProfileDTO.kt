package it.polito.server.profiles


import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size


data class ProfileDTO(

    @field:Email(message="Not a valid email address")
    @field:Size(max = 255, message = "Email too long")
    var email:String,
    @field:NotBlank(message="Not a valid username")
    @field:Size(max = 255, message = "Username too long")
    var username:String,
    @field:NotBlank(message="Not a valid name")
    @field:Size(max = 255, message = "Name too long")
    var name:String,
    @field:NotBlank(message="Not a valid surname")
    @field:Size(max = 255, message = "Surname too long")
    var surname:String,
    //var addresses : List<AddressDTO>


)

fun Profile.toDTO():ProfileDTO {
    return ProfileDTO(email, username, name, surname )
        //addresses.map { it.toDTO() })
}

fun ProfileDTO.toEntity():Profile{
    val profile = Profile()
    profile.email = email
    profile.name = name
    profile.surname = surname
    profile.username = username
   // profile.addresses=addresses.map { it.toEntity() }
    return profile
}