package it.polito.server.tickets

import it.polito.server.employees.EmployeeDTO
import it.polito.server.employees.toEntity
import it.polito.server.profiles.ProfileDTO
import it.polito.server.profiles.toEntity
import java.util.*

data class TicketToSaveDTO (
    val id: Long? = null,

    val creationDate: Date? = null,
    val lastModification: Date? = null,

    var state: StateDTO? = null,
    val customer: ProfileDTO? = null,
    val actualExpert: EmployeeDTO? = null,

    val chat: List<MessageDTO>? = null,
    var history: MutableList<HistoryToSaveDTO>? = null
)

fun TicketToSaveDTO.toEntity(): Ticket{
    val ticketToSave = Ticket()
    ticketToSave.id = id
    ticketToSave.creationDate = creationDate
    ticketToSave.lastModification = lastModification
    ticketToSave.state = state?.toEntity()
    ticketToSave.customer = customer?.toEntity()
    ticketToSave.actualExpert = actualExpert?.toEntity()
    ticketToSave.chat = chat?.map { it.toEntity() }
    ticketToSave.history = history?.map { it.toEntity() } as MutableList<History>?
    return ticketToSave
}

/*
fun Ticket.toDTO() = TicketDTO(
    id,
    creationDate,
    lastModification,
    state?.toDTO(),
    customer?.toDTO(),
    actualExpert?.toDTO(),
    chat?.map { it.toDTO() },
    history?.map { it.toHistoryFromTicketDTO() }
)*/

/*fun TicketToSaveDTO.addHistory(history: HistoryToSaveDTO){
    if(this.history == null)
        this.history = mutableListOf()

    history.ticket = this
    this.history?.add(history)
}*/

fun Ticket.addHistory(historyDTO: HistoryToSaveDTO) : Ticket{
    if(this.history == null)
        this.history = mutableListOf()

    val historyEntity = historyDTO.toEntity()
    historyEntity.ticket = this
    this.history?.add(historyEntity)
    return this
}

