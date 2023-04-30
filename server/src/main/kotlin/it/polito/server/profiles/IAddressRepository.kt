package it.polito.server.profiles
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
@Repository
interface IAddressRepository : JpaRepository<Address, Long>{

    /*
    @Query("SELECT a FROM Address a WHERE a.Profile.email = :profile")
            fun getAddressByProfile(@Param("profile") profile: Profile): List<AddressDTO>*/
}



