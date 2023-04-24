package it.polito.server.products

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class ProductDTO(

    @field:NotBlank(message="Not a valid ean")
    @field:Size(max = 23, message = "EAN too long")
    var ean: String,
    @field:NotBlank(message="Not a valid name")
    @field:Size(max = 255, message = "Product name too long")
    var name: String,
    @field:NotBlank(message="Not a valid brand")
    @field:Size(max = 255, message = "Brand name too long")
    var brand: String
)

fun ProductDTO.toEntity(): Product{
    val product = Product()
    product.id = ean
    product.name = name
    product.brand = brand
    return  product
}

fun Product.toDTO() = ProductDTO(id, name, brand)