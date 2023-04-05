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

    // todo: @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Transactional
    override fun createProduct(product: ProductDTO) {
        if(productRepository.existsById(product.ean)){
            throw ProductDuplicateException("Product with ean ${product.ean} already exists!")
        }
        productRepository.save(product.toEntity())
    }
}