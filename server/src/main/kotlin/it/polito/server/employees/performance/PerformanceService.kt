package it.polito.server.employees.performance

import it.polito.server.employees.EmployeeDTO
import it.polito.server.employees.EmployeeService
import it.polito.server.employees.IEmployeeService
import it.polito.server.tickets.ITicketRepository
import it.polito.server.tickets.enums.TimeEnum
import it.polito.server.tickets.enums.toDate
import jakarta.persistence.EntityManager
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Service

@Service
class PerformanceService(
    private val ticketRepository: ITicketRepository,
    private val employeeService: IEmployeeService
): IPerformanceService {
    private val timeslot = TimeEnum.LAST_MONTH.toDate()

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
        if (expertId == null){
            stateCount = ticketRepository.getStateCountFromDate()
            ticketsCounter = ticketRepository.getTicketsCounters(timeslot)
        }
        else{
            stateCount = ticketRepository.getStateCountFromDate(expertId)
            ticketsCounter = ticketRepository.getTicketsCounters(timeslot, expertId)
        }

        return PerformanceDTO(
            stateCount.map {it ->
                val element = it as Array<out Any>
                StateCount(
                    element[0] as String,
                    element[1] as Long
                )
            },
            (ticketsCounter as Array<out Any>).mapIndexed { id, v ->
                StateCount(
                    when(id){
                        0 -> "ticketsCreated"
                        1 -> "ticketsClosed"
                        2 -> "ticketsResolved"
                        else -> "ticketsReopened"
                    },
                    v as Long
                )
            }.toList()
        )
    }
}

