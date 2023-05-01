package it.polito.server.profiles

import it.polito.server.base.EntityBase
import jakarta.persistence.*

@Entity
class Address : EntityBase<Long>(){
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(updatable = false, nullable = false)
    var id: Long? = null
    var streetAddress: String = ""
    var number: String = ""
    var additionalInfo: String = ""     // e.g. "int. 5" or "c/o John Doe"
    var zip: String = ""
    var city: String = ""
    var region: String = ""
    var state: String = ""


    @ManyToOne
    @JoinColumn(name = "profile_email")
    var profile:Profile? = null
}