package it.polito.server.profiles

import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
class AddressController(private val addressService: IAddressService) {

    @GetMapping("/API/addresses")
    fun getAddresses() : List<AddressDTO>{
        return addressService.getAll()
    }

    @GetMapping("/API/addresses/{id}")
    fun getAddress(@PathVariable id: Long): AddressDTO?{
        return  addressService.getAddress(id)
    }

    /*
    @PostMapping("/API/addresses/")
    @ResponseStatus(HttpStatus.CREATED)
    fun createAddress(@Valid @RequestBody address: AddressDTO){
        return addressService.createAddress(address)
    }*/

    @PostMapping("/API/addresses/")
    @ResponseStatus(HttpStatus.CREATED)
    fun createAddress(
        @RequestParam email: String,
        @Valid @RequestBody address: AddressDTO
    ) {
        addressService.createAddress(email, address)
    }


    @PutMapping("/API/addresses/{id}")
    fun updateAddress(@PathVariable id: Long, @Valid @RequestBody address: AddressDTO) : AddressDTO?{
        return addressService.updateAddress(id, address)
    }

}