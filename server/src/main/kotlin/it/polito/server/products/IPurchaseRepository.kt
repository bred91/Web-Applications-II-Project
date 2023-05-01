package it.polito.server.products


import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
@Repository
public interface IPurchaseRepository: JpaRepository<Purchase, Long> {

    fun findPurchasesByCustomerEmail(email: String) : List<Purchase>
}
