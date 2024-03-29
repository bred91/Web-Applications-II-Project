package it.polito.server.tickets

import it.polito.server.tickets.priorities.PriorityDTO
import jakarta.validation.constraints.NotNull
data class StartProgressRequestDTO(
    @field:NotNull(message = "Invalid employee id")
    val employee_id:Long,
    @field:NotNull(message = "Invalid priority level")
    val priorityLevel: PriorityDTO


)
