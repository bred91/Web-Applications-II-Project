package it.polito.server.products

data class ProductDTO(
    val ean: String,
    val name: String,
    val brand: String
)

fun ProductDTO.toEntity(): Product{
    val product: Product = Product()
    product.id = ean
    product.name = name
    product.brand = brand
    return  product
}

fun Product.toDTO() = ProductDTO(id, name, brand)