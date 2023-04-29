package it.polito.server.products

import jakarta.persistence.*

@Entity
@Table(name="products")
class Product {
    @Id
    @Column(updatable = false, nullable = false)
    var id: String = ""
    var name: String = ""
    var brand: String = ""

    @OneToMany(mappedBy = "product")
    var purchases: MutableSet<Purchase>? = null
}