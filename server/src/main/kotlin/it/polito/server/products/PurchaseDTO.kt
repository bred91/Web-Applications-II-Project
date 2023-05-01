package it.polito.server.products

import it.polito.server.profiles.Profile
import jakarta.persistence.*
import java.util.*
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull


data class PurchaseDTO(


    var id: Long?,
    //@field:NotNull(message="Not a valid customer")
    //var customer: Profile?,
    var customerEmail: String?,

    //@field:NotNull(message="Not a valid product")
    //var product: Product?,
    var productId: String?,
    @field:NotNull(message="Not a valid purchase date")
    var purchaseDate: Date?,
    @field:NotBlank(message="Not a valid warrantyCode")
    var warrantyCode : String?,
    @field:NotNull(message="Not a valid expiring date")
    var expiringDate: Date?



)

fun Purchase.toDTO() : PurchaseDTO{
    return PurchaseDTO(id, customer?.email, product?.id, purchaseDate, warrantyCode, expiringDate)
}
fun PurchaseDTO.toEntity() : Purchase{
    val purchase = Purchase()
    purchase.purchaseDate=purchaseDate
    //purchase.customer=customer
    purchase.expiringDate=expiringDate
    purchase.warrantyCode=warrantyCode
    purchase.id=id
    return purchase;
}

