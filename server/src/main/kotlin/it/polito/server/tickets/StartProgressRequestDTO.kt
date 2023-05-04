package it.polito.server.tickets

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Pattern
data class StartProgressRequestDTO(
    @field:NotNull(message = "Invalid employee id")
    val employee_id:Long,
    @field:NotBlank(message = "Invalid priority level")
    @Pattern(regexp = "^(LOW|MEDIUM|HIGH)$", message = "Invalid value!")
    val priority_level:String

)
