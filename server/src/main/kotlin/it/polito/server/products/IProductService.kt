package it.polito.server.products

interface IProductService {
    fun getAll(): List<ProductDTO>
    fun getProduct(ean: String): ProductDTO?
    fun createProduct(product: ProductDTO)
    fun updateProduct(ean: String, product: ProductDTO): ProductDTO?
}