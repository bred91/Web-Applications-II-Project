package it.polito.server.profiles
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
@Repository
interface IAddressRepository : JpaRepository<Address, Long>{
    fun findByProfileEmail(email:String):List<Address>
}



