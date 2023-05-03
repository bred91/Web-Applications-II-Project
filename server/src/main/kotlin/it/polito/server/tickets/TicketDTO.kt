package it.polito.server.tickets

import it.polito.server.employees.EmployeeDTO
import it.polito.server.employees.toDTO
import it.polito.server.employees.toEntity
import it.polito.server.profiles.ProfileDTO
import it.polito.server.profiles.toDTO
import it.polito.server.profiles.toEntity
import java.util.*

data class TicketDTO (
    val id: Long? = null,

    val creationDate: Date? = null,
    val lastModification: Date? = null,

    val state: StateDTO? = null,
    val customer: ProfileDTO? = null,
    val actualExpert: EmployeeDTO? = null,

    val chat: List<MessageDTO>? = null,
    var history: List<HistoryDTO>? = null
)

fun TicketDTO.toEntity(): Ticket{
    val ticket = Ticket()
    ticket.id = id
    ticket.creationDate = creationDate
    ticket.lastModification = lastModification
    ticket.state = state?.toEntity()
    ticket.customer = customer?.toEntity()
    ticket.actualExpert = actualExpert?.toEntity()
    ticket.chat = chat?.map { it.toEntity() }
    ticket.history = history?.map { it.toEntity() } as MutableList<History>?
    return ticket
}

fun Ticket.toDTO() = TicketDTO(
    id,
    creationDate,
    lastModification,
    state?.toDTO(),
    customer?.toDTO(),
    actualExpert?.toDTO(),
    chat?.map { it.toDTO() },
    history?.map { it.toDTO() }
)