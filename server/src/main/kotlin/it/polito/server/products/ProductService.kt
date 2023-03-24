package it.polito.server.products

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

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
    }

    override fun addProduct(product: ProductDTO): Boolean {
        if(getProduct(product.ean) != null){
            return false
        }
        productRepository.save(product.toEntity())
        return true
    }
}