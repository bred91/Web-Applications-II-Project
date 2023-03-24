package it.polito.server.products

interface IProductService {
    fun getAll(): List<ProductDTO>
    fun getProduct(ean: String): ProductDTO?
    fun addProduct(product: ProductDTO): Boolean
}