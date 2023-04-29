package it.polito.server.profiles

interface IAddressService {
    fun getAll() : List<Address>
    fun getAddress(id: Long) : AddressDTO?
    fun createAddress(address: AddressDTO)
    fun updateAddress(id: Long, address: AddressDTO) : AddressDTO?
}