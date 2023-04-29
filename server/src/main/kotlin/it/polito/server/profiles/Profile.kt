package it.polito.server.profiles
import it.polito.server.products.Purchase
import it.polito.server.tickets.Ticket
import jakarta.persistence.*

@Entity
@Table(name = "profiles")
class Profile {
    @Id
    @Column(updatable = false, nullable = false)
    var email:String=""
    var username:String=""
    var name:String=""
    var surname:String=""
    var phoneNumber: String=""


    @OneToMany(mappedBy = "profile")
    var addresses = mutableSetOf<Address>()

    @OneToMany(mappedBy = "customer")
    var tickets = mutableSetOf<Ticket>()

    @OneToMany(mappedBy = "customer")
    var purchases: MutableSet<Purchase>? = null
}