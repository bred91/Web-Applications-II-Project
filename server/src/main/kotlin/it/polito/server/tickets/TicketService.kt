package it.polito.server.tickets

import it.polito.server.employees.IEmployeeRepository
import it.polito.server.employees.exception.EmployeeNotFoundException
import it.polito.server.employees.toDTO
import it.polito.server.products.PurchaseService
import it.polito.server.products.exception.PurchaseNotAssociatedException
import it.polito.server.profiles.ProfileService
import it.polito.server.tickets.enums.StateEnum
import it.polito.server.tickets.enums.toLong
import it.polito.server.tickets.exception.PriorityNotFoundException
import it.polito.server.tickets.exception.StateNotValidException
import it.polito.server.tickets.exception.TicketNotFoundException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class TicketService (private val ticketRepository: ITicketRepository,
                     private val employeeRepository: IEmployeeRepository,
                     private val priorityRepository: IPriorityRepository,
                     private val purchaseService: PurchaseService,
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
     * Method to create a new ticket
     * @param customerEmail email of the customer that creates the ticket
     * @param purchaseId id of the purchase associated to the ticket
     */
    // todo: @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    @Transactional
    override fun createTicket(customerEmail: String, purchaseId: Long) : TicketDTO? {

        val customer = profileService.getProfileByEmail(customerEmail)
        val purchase = purchaseService.getPurchaseById(purchaseId)
        if(purchase?.customerEmail != customerEmail){
            throw PurchaseNotAssociatedException("The purchase $purchaseId does not belong to $customerEmail")
        }
        val currentTimeMillis = Date()
        val newStateDTO = stateService.getStateById(StateEnum.OPEN.toLong())
        val historyDTO = HistoryDTO(null, newStateDTO, null, currentTimeMillis, null)
        val ticket = TicketDTO(null, purchase, currentTimeMillis, currentTimeMillis, newStateDTO, customer, null, null, null)
        val ticketEntity = ticket.toEntity()

        return ticketRepository.save(ticketEntity.addHistory(historyDTO.toEntity(ticketEntity))).toDTO()
    }

    /**
     * Method to start processing a ticket
     * @param idEmployee id of the employee that starts processing the ticket
     * @param priorityLevel priority level of the ticket
     */
    // todo: @PreAuthorize("hasRole('ROLE_MANAGER')")
    @Transactional
    override fun startProgress(idTicket: Long, idEmployee:Long, priorityLevel: Long ): TicketDTO? {

        val ticket = ticketRepository.findByIdOrNull(idTicket) ?: throw TicketNotFoundException("Ticket with id $idTicket not found!")
        val employee = employeeRepository.findByIdOrNull(idEmployee) ?: throw EmployeeNotFoundException("Employee with id $idEmployee doesn't exist!")
        val priority = priorityRepository.findByIdOrNull(priorityLevel) ?: throw PriorityNotFoundException("Priority Level with id $priorityLevel doesn't exist!")
        val oldStateDTO = ticket.state
        if(oldStateDTO?.id == StateEnum.OPEN.toLong() || oldStateDTO?.id == StateEnum.REOPENED.toLong()) {
            val newStateDTO = stateService.getStateById(StateEnum.IN_PROGRESS.toLong())
            val currentTimeMillis = Date()
            val historyDTO = HistoryDTO(null, newStateDTO, ticket.id, currentTimeMillis, employee.toDTO())
            ticket.actualExpert = employee
            ticket.state = newStateDTO?.toEntity()
            ticket.priority = priority
            ticket.lastModification=currentTimeMillis
            ticket.addHistory(historyDTO.toEntity(ticket))
            return ticketRepository.save(ticket).toDTO()
        }
        else throw StateNotValidException("Invalid ticket state")
    }

    /**
     * Method to stop processing a ticket -> return to OPEN state
     */
    // todo: @PreAuthorize("hasRole('ROLE_MANAGER')")
    @Transactional
    override fun stopProgress(id:Long):TicketDTO? {

        val ticket = ticketRepository.findByIdOrNull(id) ?: throw TicketNotFoundException("Ticket with id $id not found!")
        val oldStateDTO = stateService.getStateById(StateEnum.IN_PROGRESS.toLong())
        if(ticket.state?.id == oldStateDTO?.id) {
            val newStateDTO = stateService.getStateById(StateEnum.OPEN.toLong())
            val currentTimeMillis = Date()
            val historyDTO = HistoryDTO(null, newStateDTO, ticket.id, currentTimeMillis, null)

            ticket.actualExpert = null
            ticket.state = newStateDTO?.toEntity()
            ticket.lastModification=currentTimeMillis
            ticket.addHistory(historyDTO.toEntity(ticket))
            return ticketRepository.save(ticket).toDTO()
        }
        else throw StateNotValidException("Invalid ticket state")
    }

    /**
     * Method to reopen a ticket
     */
    // todo: @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    @Transactional
    override fun reopenIssue(id: Long): TicketDTO? {
        val ticket = ticketRepository.findByIdOrNull(id) ?: throw TicketNotFoundException("Ticket with id $id not found!")
        val oldStateDTO = ticket.state
        if(oldStateDTO?.id == StateEnum.CLOSED.toLong() || oldStateDTO?.id == StateEnum.RESOLVED.toLong()) {
            val newStateDTO = stateService.getStateById(StateEnum.REOPENED.toLong())
            val currentTimeMillis = Date()
            val historyDTO = HistoryDTO(null, newStateDTO, ticket.id, currentTimeMillis, ticket.actualExpert?.toDTO())
            ticket.state = newStateDTO?.toEntity()
            ticket.lastModification=currentTimeMillis
            ticket.addHistory(historyDTO.toEntity(ticket))
            return ticketRepository.save(ticket).toDTO()
        }
        else throw StateNotValidException("Invalid ticket state")
    }

    /**
     * Method to resolve a ticket
     */
    // todo: @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    // todo: @PreAuthorize("hasRole('ROLE_EXPERT')")
    @Transactional
    override fun resolveIssue(id: Long): TicketDTO? {
        val ticket = ticketRepository.findByIdOrNull(id) ?: throw TicketNotFoundException("Ticket with id $id not found!")
        val oldStateDTO = ticket.state
        if(oldStateDTO?.id != StateEnum.CLOSED.toLong() && oldStateDTO?.id != StateEnum.RESOLVED.toLong()) {
            val newStateDTO = stateService.getStateById(StateEnum.RESOLVED.toLong())
            val currentTimeMillis = Date()
            val historyDTO = HistoryDTO(null, newStateDTO, ticket.id, currentTimeMillis, ticket.actualExpert?.toDTO())
            ticket.state = newStateDTO?.toEntity()
            ticket.lastModification=currentTimeMillis
            ticket.addHistory(historyDTO.toEntity(ticket))
            return ticketRepository.save(ticket).toDTO()
        }
        else throw StateNotValidException("Invalid ticket state")
    }

    /**
     * Method to close a ticket
     */
    // todo: @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    @Transactional
    override fun closeIssue(id: Long): TicketDTO? {
        val ticket = ticketRepository.findByIdOrNull(id) ?: throw TicketNotFoundException("Ticket with id $id not found!")
        val oldStateDTO = ticket.state
        if(oldStateDTO?.id != StateEnum.CLOSED.toLong()) {
            val newStateDTO = stateService.getStateById(StateEnum.CLOSED.toLong())
            val currentTimeMillis = Date()
            val historyDTO = HistoryDTO(null, newStateDTO, ticket.id, currentTimeMillis, ticket.actualExpert?.toDTO())
            ticket.state = newStateDTO?.toEntity()
            ticket.lastModification=currentTimeMillis
            ticket.addHistory(historyDTO.toEntity(ticket))
            return ticketRepository.save(ticket).toDTO()
        }
        else throw StateNotValidException("Invalid ticket state")
    }
}