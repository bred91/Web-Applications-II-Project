package it.polito.server.employees.performance

import it.polito.server.tickets.ITicketRepository
import it.polito.server.tickets.enums.TimeEnum
import it.polito.server.tickets.enums.toDate
import jakarta.persistence.EntityManager
import org.springframework.stereotype.Service

@Service
class PerformanceService(
    private val ticketRepository: ITicketRepository,
    private val entityManager: EntityManager
): IPerformanceService {
    override fun getPerformance(): PerformanceDTO {
        val timeslot = TimeEnum.LAST_MONTH.toDate();
        return PerformanceDTO(
            ticketRepository.getStateCountFromDate(timeslot).map {it ->
                val element = it as Array<out Any>
                StateCount(
                    element[0] as String,
                    element[1] as Long
                )
            },
            (ticketRepository.getTicketsCounters(timeslot) as Array<out Any>).mapIndexed { id, v ->
                StateCount(
                    when(id){
                        0 -> "ticketsCreated"
                        1 -> "ticketsClosed"
                        2 -> "ticketsResolved"
                        else -> "ticketsReopened"
                    },
                    v as Long
                )
            }.toList(),
            StateCount(
                "ticketsWorking",
                ticketRepository.getTicketsWorking(timeslot) as Long
            )
        )
    }
}

