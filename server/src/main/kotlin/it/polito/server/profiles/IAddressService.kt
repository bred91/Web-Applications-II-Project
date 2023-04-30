package it.polito.server.profiles

interface IAddressService {
    fun getAll() : List<AddressDTO>
    fun getAddress(id: Long) : AddressDTO?
    fun createAddress(address: AddressDTO)
    fun updateAddress(id: Long, address: AddressDTO) : AddressDTO?
    //fun getAddressesByProfile(profile: Profile) : List<AddressDTO>
}