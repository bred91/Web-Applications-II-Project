package it.polito.server.employees.performance

import it.polito.server.employees.IEmployeeService
import it.polito.server.tickets.ITicketRepository
import it.polito.server.tickets.enums.TimeEnum
import it.polito.server.tickets.enums.toDate
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Service
import java.math.BigDecimal

@Service
class PerformanceService(
    private val ticketRepository: ITicketRepository,
    private val employeeService: IEmployeeService
): IPerformanceService {
    private val timeslot = TimeEnum.LAST_QUARTER.toDate()

    @PreAuthorize("hasRole('ROLE_Manager')")
    override fun getPerformance(): PerformanceDTO {
        return getPerformanceDTO()
    }

    @PreAuthorize("hasRole('ROLE_Manager')")
    override fun getExpertsPerformance(): List<ExpertPerformanceDTO>{
       val experts = employeeService.getAllExperts();
       return experts.map { e ->
           ExpertPerformanceDTO(
               getPerformanceDTO(e.id),
               e
           )
       }
    }

    private fun getPerformanceDTO(expertId: Long? = null): PerformanceDTO{
        val stateCount: Any
        val ticketsCounter: Any
        val percentageCounter: Any
        val name: String
        if (expertId == null){
            name = "Created"
            stateCount = ticketRepository.getStateCountFromDate()
            ticketsCounter = ticketRepository.getTicketsCounters(timeslot)
            percentageCounter = ticketRepository.getPercentageCounters()
        }
        else{
            name = "In Progress"
            stateCount = ticketRepository.getStateCountFromDate(expertId)
            ticketsCounter = ticketRepository.getTicketsCounters(timeslot, expertId)
            percentageCounter = ticketRepository.getPercentageCounters(expertId)
        }

        return PerformanceDTO(
            stateCount.map {
                val element = it as Array<out Any>
                StateCount(
                    element[0] as String,
                    element[1] as Long
                )
            },
            (ticketsCounter as Array<out Any>).mapIndexed { id, v ->
                StateCount(
                    when (id) {
                        0 -> name
                        1 -> "Closed"
                        2 -> "Resolved"
                        else -> "Reopened"
                    },
                    v as Long
                )
            }.toList(),
            (percentageCounter as Array<out Any>).mapIndexed { id, v ->
                    val count = when (v) {
                        is Long -> v
                        is BigDecimal -> v.toLong()
                        else -> throw IllegalArgumentException("Unsupported type: ${v.javaClass}")
                    }
                    StateCount(
                        when (id) {
                            0 -> "Closed"
                            else -> "Resolved"
                        },
                        count
                    )
            }.toList()
        )
    }
}

