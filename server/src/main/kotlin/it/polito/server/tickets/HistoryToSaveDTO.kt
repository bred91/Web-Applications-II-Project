package it.polito.server.tickets

import it.polito.server.employees.EmployeeDTO
import it.polito.server.employees.toEntity
import jakarta.validation.constraints.NotNull
import java.util.*

/*data class HistoryToSaveDTO(

    var id: Long?,
    @field:NotNull(message="Not a valid state")
    var state: StateDTO?,
    @field:NotNull(message="Not a valid ticket")
    var ticket: TicketToSaveDTO?,
    @field:NotNull(message="Not a valid date")
    var timestamp: Date?,
    @field:NotNull(message="Not a valid expert")
    var expert: EmployeeDTO?
)*/

data class HistoryToSaveDTO(

    var id: Long?,
    @field:NotNull(message="Not a valid state")
    var state: StateDTO?,
    @field:NotNull(message="Not a valid ticket")
    var ticket: TicketDTO?,
    @field:NotNull(message="Not a valid date")
    var timestamp: Date?,
    @field:NotNull(message="Not a valid expert")
    var expert: EmployeeDTO?
)

fun HistoryToSaveDTO.toEntity() : History{
    val history = History()
    history.id= id
    history.state = state?.toEntity()
    history.ticket= ticket?.toEntity()
    history.timestamp = timestamp
    history.expert = expert?.toEntity()
    return history
}

/*fun History.toHistoryFromTicketDTO(): HistoryToSaveDTO {
    return HistoryToSaveDTO(id, state, timestamp, expert)
}*/
