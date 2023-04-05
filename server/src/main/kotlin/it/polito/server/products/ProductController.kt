package it.polito.server.products

import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
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
        //if(product != null)
        productService.createProduct(product)
        // todo: else throw exception
    }
}