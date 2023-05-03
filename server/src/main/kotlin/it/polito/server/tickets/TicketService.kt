package it.polito.server.tickets

import it.polito.server.employees.IEmployeeRepository
import it.polito.server.employees.toDTO
import it.polito.server.tickets.enums.StateEnum
import it.polito.server.tickets.enums.toLong
import it.polito.server.tickets.exception.NullTicketIdException
import it.polito.server.tickets.exception.TicketNotFoundException
import it.polito.server.Exception.NotFoundException
import it.polito.server.profiles.ProfileService
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.sql.Timestamp
import java.util.Date

@Service
class TicketService (private val ticketRepository: ITicketRepository,
                     private val employeeRepository: IEmployeeRepository,
                     private val profileService: ProfileService,
    private val stateService: StateService) : ITicketService {

    override fun getTicketById(id: Long): TicketDTO? {
        val t = ticketRepository.findByIdOrNull(id)
            ?: throw TicketNotFoundException("Ticket with id $id not found")
        return t.toDTO()
    }


    override fun getTickets(): List<TicketDTO> {
        return ticketRepository.findAll().map { it.toDTO() }
    }


    /**
     * From the client we expect a new Ticket with the state field set to NULL
     */
    // todo: @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    @Transactional
    override fun createTicket(customerEmail: String) : TicketDTO? {
        val customer = profileService.getProfile(customerEmail)
        val currentTimeMillis = Date()
        val newStateDTO = stateService.getState(StateEnum.OPEN.toLong())
        val historyDTO = HistoryDTO(null, newStateDTO, null, currentTimeMillis, null)
        var ticket = TicketDTO(null, currentTimeMillis, currentTimeMillis, newStateDTO, customer, null, null, null)
        var ticket_entity = ticket.toEntity()
        //ticket_entity = ticket_entity.addHistory(historyDTO.toEntity(ticket_entity))
        return ticketRepository.save(ticket_entity.addHistory(historyDTO.toEntity(ticket_entity))).toDTO()
    }



    @Transactional
    override fun startProgress(id_ticket: Long, id_employee:Long ): TicketDTO? {
        val ticket = ticketRepository.findByIdOrNull(id_ticket) ?: throw TicketNotFoundException("Ticket with id $id_ticket not found!")
        val employee = employeeRepository.findByIdOrNull(id_employee) ?: throw NotFoundException("Employee with id $id_employee doesn't exist!")
        val oldStateDTO = ticket.state
        if(oldStateDTO?.id == StateEnum.OPEN.toLong() || oldStateDTO?.id == StateEnum.REOPENED.toLong()) {
            val newStateDTO = stateService.getState(StateEnum.IN_PROGRESS.toLong())
            val currentTimeMillis = Date()
            val historyDTO = HistoryDTO(null, newStateDTO, ticket.id, currentTimeMillis, employee.toDTO())
            ticket.actualExpert = employee
            ticket.state = newStateDTO?.toEntity()
            ticket.lastModification=currentTimeMillis
            ticket.addHistory(historyDTO.toEntity(ticket))
            return ticketRepository.save(ticket).toDTO()
        }
        else throw IllegalStateException("Invalid ticket state")
    }




    override fun stopProgress(id:Long):TicketDTO? {
        val ticket = ticketRepository.findByIdOrNull(id) ?: throw TicketNotFoundException("Ticket with id $id not found!")
        val oldStateDTO = stateService.getState(StateEnum.IN_PROGRESS.toLong())
        if(ticket.state?.id == oldStateDTO?.id) {
            val newStateDTO = stateService.getState(StateEnum.OPEN.toLong())
            val currentTimeMillis = Date()
            val historyDTO = HistoryDTO(null, newStateDTO, ticket.id, currentTimeMillis, null)

            ticket.actualExpert = null
            ticket.state = newStateDTO?.toEntity()
            ticket.lastModification=currentTimeMillis
            ticket.addHistory(historyDTO.toEntity(ticket))
            return ticketRepository.save(ticket).toDTO()
        }
        else throw IllegalStateException("Invalid ticket state")
    }


    override fun reopenIssue(id: Long): TicketDTO? {
        val ticket = ticketRepository.findByIdOrNull(id) ?: throw TicketNotFoundException("Ticket with id $id not found!")
        val oldStateDTO = ticket.state
        if(oldStateDTO?.id == StateEnum.CLOSED.toLong() || oldStateDTO?.id == StateEnum.RESOLVED.toLong()) {
            val newStateDTO = stateService.getState(StateEnum.REOPENED.toLong())
            val currentTimeMillis = Date()
            val historyDTO = HistoryDTO(null, newStateDTO, ticket.id, currentTimeMillis, ticket.actualExpert?.toDTO())
            ticket.state = newStateDTO?.toEntity()
            ticket.lastModification=currentTimeMillis
            ticket.addHistory(historyDTO.toEntity(ticket))
            return ticketRepository.save(ticket).toDTO()
        }
        else throw IllegalStateException("Invalid ticket state")
    }


    override fun resolveIssue(id: Long): TicketDTO? {
        val ticket = ticketRepository.findByIdOrNull(id) ?: throw TicketNotFoundException("Ticket with id $id not found!")
        val oldStateDTO = ticket.state
        if(oldStateDTO?.id != StateEnum.CLOSED.toLong() || oldStateDTO?.id != StateEnum.RESOLVED.toLong()) {
            val newStateDTO = stateService.getState(StateEnum.RESOLVED.toLong())
            val currentTimeMillis = Date()
            val historyDTO = HistoryDTO(null, newStateDTO, ticket.id, currentTimeMillis, ticket.actualExpert?.toDTO())
            ticket.state = newStateDTO?.toEntity()
            ticket.lastModification=currentTimeMillis
            ticket.addHistory(historyDTO.toEntity(ticket))
            return ticketRepository.save(ticket).toDTO()
        }
        else throw IllegalStateException("Invalid ticket state")
    }


    override fun closeIssue(id: Long): TicketDTO? {
        val ticket = ticketRepository.findByIdOrNull(id) ?: throw TicketNotFoundException("Ticket with id $id not found!")
        val oldStateDTO = ticket.state
        if(oldStateDTO?.id != StateEnum.CLOSED.toLong()) {
            val newStateDTO = stateService.getState(StateEnum.CLOSED.toLong())
            val currentTimeMillis = Date()
            val historyDTO = HistoryDTO(null, newStateDTO, ticket.id, currentTimeMillis, ticket.actualExpert?.toDTO())
            ticket.state = newStateDTO?.toEntity()
            ticket.lastModification=currentTimeMillis
            ticket.addHistory(historyDTO.toEntity(ticket))
            return ticketRepository.save(ticket).toDTO()
        }
        else throw IllegalStateException("Invalid ticket state")
    }


}