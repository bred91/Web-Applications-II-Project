package it.polito.server.tickets

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ITicketRepository : JpaRepository<Ticket, Long> {
    fun findByActualExpertId(actualExpertId: Long):List<Ticket>

    fun findByCustomerEmail(customerEmail : String) : List<Ticket>

    fun findByActualExpert_Email(email: String): List<Ticket>
}