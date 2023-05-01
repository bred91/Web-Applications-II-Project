package it.polito.server.products

interface IPurchaseService {
    fun getAll() : List<PurchaseDTO>
    fun getPurchaseById(id: Long) : PurchaseDTO?
    fun createPurchase(email:String, productId:String, purchase: PurchaseDTO)
    fun updatePurchase(id: Long, purchase: PurchaseDTO) : PurchaseDTO?
}