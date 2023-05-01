package it.polito.server.tickets

import it.polito.server.employees.Employee
import it.polito.server.profiles.Profile
import java.util.*

data class JustTicketDTO (
    val id: Long? = null,

    val creationDate: Date? = null,
    val lastModification: Date? = null,

    /*val state: StateDTO? = null,*/
    val state: Long,
    val customer: String,
    val actualExpert: Long
)

fun JustTicketDTO.toEntity(): Ticket{
    val justTicket = Ticket()
    justTicket.id = id
    justTicket.creationDate = creationDate
    justTicket.lastModification = lastModification
    justTicket.state = State().apply { id = state }
    justTicket.customer = Profile().apply { email = customer }
    justTicket.actualExpert = Employee().apply { id = actualExpert }
    return justTicket
}

fun Ticket.toJustDTO() : JustTicketDTO{
    return JustTicketDTO(
        id,
        creationDate,
        lastModification,
        state?.id ?: -1,
        customer?.email ?: "",
        actualExpert?.id ?: -1
    )
}

