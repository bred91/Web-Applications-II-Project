package it.polito.server.products

interface IPurchaseService {
    fun getAll() : List<PurchaseDTO>
    fun getPurchase(id: Long) : PurchaseDTO?
    fun createPurchase(purchase: PurchaseDTO)
    fun updatePurchase(id: Long, purchase: PurchaseDTO) : PurchaseDTO?
}