package it.polito.server.tickets.history

import it.polito.server.employees.EmployeeDTO
import it.polito.server.employees.toDTO
import it.polito.server.employees.toEntity
import it.polito.server.tickets.states.StateDTO
import it.polito.server.tickets.Ticket
import it.polito.server.tickets.states.toDTO
import it.polito.server.tickets.states.toEntity
import jakarta.validation.constraints.NotNull
import java.util.*


data class HistoryDTO(

    var id: Long?,
    @field:NotNull(message="Not a valid state")
    var state: StateDTO?,
    @field:NotNull(message="Not a valid ticket")
    //var ticket: TicketDTO?,
    /*@field:NotNull(message="Not a valid state")
    var state_id: Long?,
    @field:NotNull(message="Not a valid ticket")
    var ticket_id: Long?,*/
    var ticket_id:Long?,
    @field:NotNull(message="Not a valid date")
    var timestamp: Date?,
    @field:NotNull(message="Not a valid expert")
    var expert: EmployeeDTO?
)


fun HistoryDTO.toEntity(ticket: Ticket): History {
    val history = History()
    history.id = id
    history.state = state?.toEntity()
    history.ticket = ticket
    history.timestamp = timestamp
    history.expert = expert?.toEntity()
    return history
}

fun History.toDTO(): HistoryDTO {
    return HistoryDTO(
        id,
        state?.toDTO(),
        ticket?.id,
        timestamp,
        expert?.toDTO()
    )
}


/*fun HistoryDTO.toEntity() : History{
    val history = History()
    history.id= id
    history.state = state?.toEntity()
    history.ticket=
    ticket.history = historyIds?.mapNotNull { id ->
        getHistoryById(id)
    }?.toMutableList()
    history.timestamp = timestamp
    history.expert = expert?.toEntity()
    return history
}*/


/*fun toDTO(history:History) : HistoryDTO {
    return HistoryDTO(
        id = history.id,
        state = history.state?.toDTO(),
        history.ticket?.id,
        timestamp = history.timestamp,
        expert = history.expert?.toDTO()
    )
}*/


/*fun History.toDTO() : HistoryDTO{
    return HistoryDTO(id, state?.toDTO(), ticket?.toDTO(), timestamp, expert?.toDTO())
}*/