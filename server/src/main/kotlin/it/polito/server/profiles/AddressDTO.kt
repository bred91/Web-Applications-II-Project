package it.polito.server.profiles
import jakarta.validation.constraints.NotBlank

data class AddressDTO(

    var id: Long?,
    @field:NotBlank(message="Not a valid address")
    var streetAddress: String,
    @field:NotBlank(message="Not a valid number")
    var number: String,
    var additionalInfo: String,
    @field:NotBlank(message="Not a valid zip code")
    var zip: String,
    @field:NotBlank(message="Not a valid city")
    var city: String,
    @field:NotBlank(message="Not a valid region")
    var region: String,
    @field:NotBlank(message="Not a valid state")
    var state: String,
    /*@field:NotNull(message="Not a valid profile")
    var profile: Profile*/
)

fun Address.toDTO() : AddressDTO {
    return AddressDTO(id, streetAddress, number, additionalInfo, zip, city, region, state)
}
fun AddressDTO.toEntity() : Address{
    val address = Address()
    address.id = id
    //address.profile = profile
    address.additionalInfo=additionalInfo
    address.zip = zip
    address.city = city
    address.region = region
    address.state = state
    address.number=number
    address.streetAddress= streetAddress
    return address;
}