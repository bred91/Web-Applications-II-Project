package it.polito.server.profiles
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
@Repository
interface IAddressRepository : JpaRepository<Address, String>{
}