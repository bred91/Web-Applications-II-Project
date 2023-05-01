package it.polito.server.tickets

import it.polito.server.employees.Employee
import jakarta.validation.constraints.NotNull
import java.util.*


data class HistoryDTO(

    var id: Long?,
    @field:NotNull(message="Not a valid state")
    var state: State?,
    @field:NotNull(message="Not a valid ticket")
    var ticket: Ticket?,
    @field:NotNull(message="Not a valid date")
    var timestamp: Date?,
    @field:NotNull(message="Not a valid expert")
    var expert: Employee?
)

fun HistoryDTO.toEntity() : History{
    val history = History()
    history.id= id
    history.state = state
    history.ticket=ticket
    history.timestamp = timestamp
    history.expert = expert
    return history
}

fun History.toDTO() = HistoryDTO(id, state, ticket, timestamp, expert)
