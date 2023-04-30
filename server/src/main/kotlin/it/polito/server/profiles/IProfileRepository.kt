package it.polito.server.profiles
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface IProfileRepository:JpaRepository<Profile, String>{
    fun findAddressesByemail(email:String):MutableSet<Address>
}