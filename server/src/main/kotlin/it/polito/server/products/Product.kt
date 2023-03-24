package it.polito.server.products

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name="products")
class Product {
    @Id
    var id: String = ""
    var name: String = ""
    var brand: String = ""
}