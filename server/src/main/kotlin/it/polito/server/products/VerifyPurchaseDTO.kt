package it.polito.server.products

import jakarta.validation.constraints.NotBlank

data class VerifyPurchaseDTO(
    @field:NotBlank(message="Not a valid ean")
    var ean : String,
    @field:NotBlank(message="Not a valid warrantyCode")
    var warrantyCode : String,
)
