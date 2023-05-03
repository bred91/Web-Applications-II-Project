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
    //var history: List<HistoryDTO>? = null
    var history : MutableList<HistoryDTO>? = mutableListOf()
    //var historyIds : List<Long>? = emptyList()
)

fun TicketDTO.toEntity(): Ticket {
    val ticket = Ticket()
    ticket.id = id
    ticket.creationDate = creationDate
    ticket.lastModification = lastModification
    ticket.state = state?.toEntity()
    ticket.customer = customer?.toEntity()
    ticket.actualExpert = actualExpert?.toEntity()
    ticket.chat = chat?.map { it.toEntity()}
    ticket.history = history?.map{it.toEntity(ticket)}?.toMutableList()
    return ticket
}

fun Ticket.toDTO(): TicketDTO {
    return TicketDTO(
        id,
        creationDate,
        lastModification,
        state?.toDTO(),
        customer?.toDTO(),
        actualExpert?.toDTO(),
        chat?.map{it.toDTO()},
        history?.map { it.toDTO() }?.toMutableList()
    )
}

/*fun toDTO(ticket: Ticket, historyId: Long?): TicketDTO {
    val historyIds: MutableList<Long>? = null
    if (historyId != null) {
        historyIds?.add(historyId)
    }
    return TicketDTO(
        ticket.id,
        ticket.creationDate,
        ticket.lastModification,
        ticket.state?.toDTO(),
        ticket.customer?.toDTO(),
        ticket.actualExpert?.toDTO(),
        ticket.chat?.map { it.toDTO() },
        historyIds
    )
}*/

/*fun TicketDTO.toEntity(): Ticket{
    val ticket = Ticket()
    ticket.id = id
    ticket.creationDate = creationDate
    ticket.lastModification = lastModification
    ticket.state = state?.toEntity()
    ticket.customer = customer?.toEntity()
    ticket.actualExpert = actualExpert?.toEntity()
    ticket.chat = chat?.map { it.toEntity() }
    ticket.history = historyIds?.mapNotNull { id ->
        getHistoryById(id)
    }?.toMutableList()
    return ticket
}

private fun getHistoryById(id: Long): History? {
    // Query the database for the History entity with the given id
    return null // Replace with actual implementation
}



fun toDTO(ticket:Ticket):TicketDTO {
    return TicketDTO(
        ticket.id,
        ticket.creationDate,
        ticket.lastModification,
        ticket.state?.toDTO(),
        ticket.customer?.toDTO(),
        ticket.actualExpert?.toDTO(),
        ticket.chat?.map { it.toDTO() },
        ticket.history?.mapNotNull { it.id }
    )
}*/


/*
fun Ticket.toDTO() = TicketDTO(
    id,
    creationDate,
    lastModification,
    state?.toDTO(),
    customer?.toDTO(),
    actualExpert?.toDTO(),
    chat?.map { it.toDTO() },
    history?.map { toDTO(it) }
)*/