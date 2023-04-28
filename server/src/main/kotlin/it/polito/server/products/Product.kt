package it.polito.server.products

import it.polito.server.profiles.Purchase
import jakarta.persistence.*

@Entity
@Table(name="products")
class Product {
    @Id
    @Column(updatable = false, nullable = false)
    var id: String = ""
    var name: String = ""
    var brand: String = ""

    @OneToMany(mappedBy = "ean")
    var purchases: MutableSet<Purchase>? = null
}