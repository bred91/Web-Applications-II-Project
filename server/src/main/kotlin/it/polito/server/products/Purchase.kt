package it.polito.server.products

import it.polito.server.base.EntityBase
import it.polito.server.profiles.Profile
import jakarta.persistence.*
import java.util.*

@Entity
class Purchase : EntityBase<Long>(){
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(updatable = false, nullable = false)
    var id: Long? = null

    @ManyToOne
    var customer: Profile? = null

    @ManyToOne
    var ean: Product? = null

    @Column(updatable = false, nullable = false)
    var purchaseDate: Date? = null
    @Column(updatable = false, nullable = false)
    var warrantyCode : String? = null
    @Column(updatable = false, nullable = false)
    @Temporal(TemporalType.DATE)
    var expiringDate: Date? = null
}