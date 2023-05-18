package it.polito.server.products

import it.polito.server.products.exception.ProductDuplicateException
import it.polito.server.products.exception.ProductNotFoundException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ProductService(
    private val productRepository: IProductRepository
): IProductService {

    override fun getAll(): List<ProductDTO> {
        return productRepository.findAll()
            .map{it.toDTO()}
    }

    override fun getProduct(ean: String): ProductDTO? {
        return productRepository.findByIdOrNull(ean)
            ?.toDTO()
            ?: throw ProductNotFoundException("Product with ean $ean not found")
    }


    @Transactional
    override fun createProduct(product: ProductDTO) {
        if(productRepository.existsById(product.ean)){
            throw ProductDuplicateException("Product with ean ${product.ean} already exists!")
        }
        productRepository.save(product.toEntity())
    }


    @Transactional
    override fun updateProduct(ean: String, product: ProductDTO): ProductDTO? {
        return when (productRepository.findByIdOrNull(ean)?.toDTO()) {
            null -> {throw ProductNotFoundException("Profile with email $ean not found")}
            else -> {productRepository.save(product.toEntity()).toDTO()}
        }
    }


}