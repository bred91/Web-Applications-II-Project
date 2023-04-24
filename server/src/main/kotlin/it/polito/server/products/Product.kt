package it.polito.server.products

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name="products")
class Product {
    @Id
    @Column(updatable = false, nullable = false)
    var id: String = ""
    var name: String = ""
    var brand: String = ""
}