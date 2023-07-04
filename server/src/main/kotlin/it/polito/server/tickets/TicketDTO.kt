package it.polito.server.tickets

import it.polito.server.employees.EmployeeDTO
import it.polito.server.employees.toDTO
import it.polito.server.employees.toEntity
import it.polito.server.products.PurchaseDTO
import it.polito.server.products.toDTO
import it.polito.server.products.toEntity
import it.polito.server.profiles.ProfileDTO
import it.polito.server.profiles.toDTO
import it.polito.server.profiles.toEntity
import it.polito.server.tickets.history.HistoryDTO
import it.polito.server.tickets.history.toDTO
import it.polito.server.tickets.history.toEntity
import it.polito.server.tickets.priorities.PriorityDTO
import it.polito.server.tickets.priorities.toDTO
import it.polito.server.tickets.priorities.toEntity
import it.polito.server.tickets.states.StateDTO
import it.polito.server.tickets.states.toDTO
import it.polito.server.tickets.states.toEntity
import java.util.*

data class TicketDTO (
    val id: Long? = null,
    val purchase: PurchaseDTO? = null,
    val creationDate: Date? = null,
    val lastModification: Date? = null,
    val state: StateDTO? = null,
    val customer: ProfileDTO? = null,
    val actualExpert: EmployeeDTO? = null,
    val priorityLevel: PriorityDTO? = null,
    var history : MutableList<HistoryDTO>? = mutableListOf()
)

fun TicketDTO.toEntity(): Ticket {
    val ticket = Ticket()
    ticket.id = id
    ticket.purchase = purchase?.toEntity(customer?.toEntity() )
    ticket.creationDate = creationDate
    ticket.lastModification = lastModification
    ticket.state = state?.toEntity()
    ticket.customer = customer?.toEntity()
    ticket.actualExpert = actualExpert?.toEntity()
    ticket.priority = priorityLevel?.toEntity()
    ticket.history = history?.map{it.toEntity(ticket)}?.toMutableList()
    return ticket
}

fun Ticket.toDTO(): TicketDTO {
    return TicketDTO(
        id,
        purchase?.toDTO(),
        creationDate,
        lastModification,
        state?.toDTO(),
        customer?.toDTO(),
        actualExpert?.toDTO(),
        priority?.toDTO(),
        history?.map { it.toDTO() }?.toMutableList()
    )
}

