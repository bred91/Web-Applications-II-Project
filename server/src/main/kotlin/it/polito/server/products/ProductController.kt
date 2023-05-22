package it.polito.server.products

import io.micrometer.observation.annotation.Observed
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@Observed
class ProductController(
    private val productService: ProductService
) {
    @GetMapping("/API/products")
    fun getAll(): List<ProductDTO>{
        return productService.getAll()
    }

    @GetMapping("/API/products/{ean}")
    fun getProduct(@PathVariable ean: String): ProductDTO?{
        return productService.getProduct(ean)
    }

    @PostMapping("/API/products")
    @ResponseStatus(HttpStatus.CREATED)
    fun createProduct(@Valid @RequestBody product: ProductDTO){
        productService.createProduct(product)
    }

    @PutMapping("/API/products/{ean}")
    fun updateProduct(@PathVariable ean: String,
                      @Valid @RequestBody product: ProductDTO): ProductDTO?{
        return productService.updateProduct(ean, product)
    }
}