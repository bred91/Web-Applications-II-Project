package it.polito.server.products

import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
class PurchaseController(private val purchaseService:PurchaseService) {
    @PostMapping("/API/purchases/profiles/{email}/products/{productId}")
    @ResponseStatus(HttpStatus.CREATED)
    fun createPurchase(@PathVariable email: String, @PathVariable productId: String, @Valid @RequestBody purchase: PurchaseDTO){
        return purchaseService.createPurchase(email, productId, purchase)
    }

    @GetMapping("/API/purchases")
    fun getAll(): List<PurchaseDTO>{
        return purchaseService.getAll()
    }

    @GetMapping("/API/purchases/{id}")
    fun getPurchaseById(@PathVariable id: Long): PurchaseDTO?{
        return purchaseService.getPurchaseById(id)
    }


}