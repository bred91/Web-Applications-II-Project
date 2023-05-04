package it.polito.server.products

import it.polito.server.Exception.NotFoundException
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
            ?: throw NotFoundException("Purchase with id $id does not exist")
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
        TODO("Not yet implemented")
    }


}