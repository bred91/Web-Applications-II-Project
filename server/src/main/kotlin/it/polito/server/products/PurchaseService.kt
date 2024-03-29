package it.polito.server.products

import it.polito.server.products.exception.PurchaseNotFoundException
import it.polito.server.profiles.ProfileService
import it.polito.server.profiles.toEntity
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.util.*
@Service
class PurchaseService(private val purchaseRepository: IPurchaseRepository,
                     private val profileService: ProfileService,
                     private val productService: ProductService
    ): IPurchaseService{
    override fun getAll(): List<PurchaseDTO> {
       return purchaseRepository.findAll().map { it.toDTO() }
    }

    override fun getPurchaseById(id: Long): PurchaseDTO? {
        return purchaseRepository.findByIdOrNull(id)?.toDTO()
            ?: throw PurchaseNotFoundException("Purchase with id $id does not exist")
    }

    override fun createPurchase(email:String, productId: String, purchase: PurchaseDTO) {
        val profile = profileService.getProfileByEmail(email)
        val product = productService.getProduct(productId)
        var purchase = purchase.toEntity(profile?.toEntity())
        purchase.customer=profile?.toEntity()
        purchase.product=product?.toEntity()
        purchase.purchaseDate= Date()
        purchaseRepository.save(purchase)
    }


    override fun updatePurchase(id: Long, purchase: PurchaseDTO): PurchaseDTO? {
        val purchaseEntity = purchaseRepository.findByIdOrNull(id)
            ?: throw PurchaseNotFoundException("Purchase with id $id does not exist")
        purchaseEntity.purchaseDate = purchase.purchaseDate
        purchaseEntity.expiringDate=purchase.expiringDate
        purchaseEntity.warrantyCode=purchase.warrantyCode
        purchaseEntity.customer = purchase.customerEmail?.let { profileService.getProfileByEmail(it)?.toEntity() }
        purchaseEntity.product = purchase.product?.toEntity()
        return purchaseRepository.save(purchaseEntity).toDTO()
    }

    override fun verifyPurchase(ean: String, warrantyCode: String): PurchaseDTO?{
        return purchaseRepository.findPurchaseByWarrantyCodeAndProduct_Id(warrantyCode, ean)?.toDTO()
    }
}