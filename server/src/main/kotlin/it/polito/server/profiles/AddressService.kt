package it.polito.server.profiles
import it.polito.server.Exception.*
import it.polito.server.profiles.exception.ProfileNotFoundException
import org.springframework.transaction.annotation.Transactional
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class AddressService (private val addressRepository: IAddressRepository, private val profileRepository: IProfileRepository) : IAddressService{
    override fun getAll(): List<AddressDTO> {
        return addressRepository.findAll().map { it.toDTO() }
    }

    override fun getAddress(id: Long): AddressDTO? {
        return addressRepository.findByIdOrNull(id)?.toDTO()
        ?: throw NotFoundException("Address with id $id not found")
    }

    @Transactional
    override fun createAddress(address: AddressDTO) {

        addressRepository.save(address.toEntity())
        /*when(profileRepository.existsById(address.profile.email)) {
                true -> addressRepository.save(address.toEntity())
                false -> throw ProfileNotFoundException("Customer with id ${address.profile.email} doesn't exists")
        }*/

    }

    @Transactional
    override fun updateAddress(id: Long, address: AddressDTO): AddressDTO? {
        return when (addressRepository.findByIdOrNull(id)?.toDTO()){
            null -> {throw NotFoundException("Address with id $id not found")}
            else -> {
                addressRepository.save(address.toEntity()).toDTO()
            }
        }
    }





}