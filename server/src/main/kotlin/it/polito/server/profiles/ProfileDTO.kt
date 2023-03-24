data class ProfileDTO(
    val ean:String,
    val name:String,
    val brand:String
)

fun Profile.toDTO():ProfileDTO {
    return ProductDTO(ean, name, brand)
}