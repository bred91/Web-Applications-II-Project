package it.polito.server.profiles
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "profiles")
class Profile {
    @Id
    var email:String=""
    var username:String=""
    var name:String=""
    var surname:String=""
}