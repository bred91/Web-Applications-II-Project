package it.polito.server.tickets

import it.polito.server.employees.IEmployeeRepository
import it.polito.server.employees.exception.EmployeeNotFoundException
import it.polito.server.employees.toDTO
import it.polito.server.products.PurchaseService
import it.polito.server.products.exception.PurchaseNotAssociatedException
import it.polito.server.profiles.ProfileService
import it.polito.server.tickets.enums.StateEnum
import it.polito.server.tickets.enums.toLong
import it.polito.server.tickets.exception.AuthorizationServiceException
import it.polito.server.tickets.exception.PriorityNotFoundException
import it.polito.server.tickets.exception.StateNotValidException
import it.polito.server.tickets.exception.TicketNotFoundException
import it.polito.server.tickets.history.HistoryDTO
import it.polito.server.tickets.history.toEntity
import it.polito.server.tickets.priorities.IPriorityRepository
import it.polito.server.tickets.states.StateService
import it.polito.server.tickets.states.toEntity
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.security.Principal
import java.util.*

@Service
class TicketService (private val ticketRepository: ITicketRepository,
                     private val employeeRepository: IEmployeeRepository,
                     private val priorityRepository: IPriorityRepository,
                     private val purchaseService: PurchaseService,
                     private val profileService: ProfileService,
                     private val stateService: StateService
) : ITicketService {

    @PreAuthorize("hasRole('ROLE_Manager')")
    override fun getTicketById(id: Long): TicketDTO? {
        val t = ticketRepository.findByIdOrNull(id)
            ?: throw TicketNotFoundException("Ticket with id $id not found")
        return t.toDTO()
    }


    @PreAuthorize("hasAnyRole('ROLE_Manager', 'ROLE_Expert', 'ROLE_Client')")
    override fun getTickets(): List<TicketDTO> {
        val jwt = SecurityContextHolder.getContext().authentication.principal as Jwt
        val userEmail = jwt.getClaim("email") as String
        val auth = SecurityContextHolder.getContext().authentication
        if (auth == null) {
            throw AuthorizationServiceException("No authorization")
        }
        when {
            auth.authorities.any { it.authority.equals("ROLE_Client")} -> {
                return ticketRepository.findByCustomerEmail(userEmail).map { it.toDTO() }
            }
            auth.authorities.any { it.authority.equals("ROLE_Expert")} -> {
                return ticketRepository.findByActualExpert_Email(userEmail).map { it.toDTO() }
            }
//            auth.authorities.any { it.authority.equals("ROLE_Manager")} -> {
//                return ticketRepository.findAll().map { it.toDTO() }
//            }
            else -> return ticketRepository.findAll().map { it.toDTO() }
        }
    }


    /**
     * Method to create a new ticket
     * @param customerEmail email of the customer that creates the ticket
     * @param purchaseId id of the purchase associated to the ticket
     */
    /*@PreAuthorize("hasRole('ROLE_Client')")
    @Transactional
    override fun createTicket(purchaseId: Long) : TicketDTO? {

        val userEmail = SecurityContextHolder.getContext().authentication.name

        val customer = profileService.getProfileByEmail(userEmail)
        val purchase = purchaseService.getPurchaseById(purchaseId)
        if(purchase?.customerEmail != userEmail){
            throw PurchaseNotAssociatedException("The purchase $purchaseId does not belong to $customerEmail")
        }
        val currentTimeMillis = Date()
        val newStateDTO = stateService.getStateById(StateEnum.OPEN.toLong())
        val historyDTO = HistoryDTO(null, newStateDTO, null, currentTimeMillis, null)
        val ticket = TicketDTO(null, purchase, currentTimeMillis, currentTimeMillis, newStateDTO, customer, null, null, null)
        val ticketEntity = ticket.toEntity()

        return ticketRepository.save(ticketEntity.addHistory(historyDTO.toEntity(ticketEntity))).toDTO()
    }*/

    @PreAuthorize("hasRole('ROLE_Client')")
    @Transactional
    override fun createTicket(purchaseId: Long) : TicketDTO? {

        val jwt = SecurityContextHolder.getContext().authentication.principal as Jwt
        val userEmail = jwt.getClaim("email") as String

        val customer = profileService.getProfileByEmail(userEmail)
        val purchase = purchaseService.getPurchaseById(purchaseId)
        if(purchase?.customerEmail != userEmail){
            throw PurchaseNotAssociatedException("The purchase $purchaseId does not belong to $userEmail")
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
    @PreAuthorize("hasRole('ROLE_Manager')")
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
        else throw StateNotValidException("Invalid Request: The ticket is in state ${oldStateDTO?.name}")
    }

    /**
     * Method to stop processing a ticket -> return to OPEN state
     */

    @PreAuthorize("hasAnyRole('ROLE_Manager', 'ROLE_Expert')")
    @Transactional
    override fun stopProgress(id:Long):TicketDTO? {

        val ticket = ticketRepository.findByIdOrNull(id) ?: throw TicketNotFoundException("Ticket with id $id not found!")
        val jwt = SecurityContextHolder.getContext().authentication.principal as Jwt
        val userEmail = jwt.getClaim("email") as String
        val auth = SecurityContextHolder.getContext().authentication
        if (auth != null && auth.authorities.any { it.authority.equals("ROLE_Expert")} && ticket.actualExpert?.email != userEmail) {
                throw AuthorizationServiceException("The expert logged in is not the expert assigned to ticket")
        }

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
        else throw StateNotValidException("Invalid Request: The ticket is in state ${ticket.state?.name}")
    }

    /**
     * Method to reopen a ticket
     */
    @PreAuthorize("hasRole('ROLE_Client')")
    @Transactional
    override fun reopenIssue(id: Long): TicketDTO? {
        val ticket = ticketRepository.findByIdOrNull(id) ?: throw TicketNotFoundException("Ticket with id $id not found!")
        val jwt = SecurityContextHolder.getContext().authentication.principal as Jwt
        val userEmail = jwt.getClaim("email") as String
        if(ticket.customer?.email != userEmail){
            throw AuthorizationServiceException("The customer logged in is not the customer who has opened the ticket")
        }
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
        else throw throw StateNotValidException("Invalid Request: The ticket is in state ${ticket.state?.name}")
    }

    /**
     * Method to resolve a ticket
     */

    @PreAuthorize("hasAnyRole('ROLE_Expert', 'ROLE_Client')")
    @Transactional
    override fun resolveIssue(id: Long): TicketDTO? {
        val ticket = ticketRepository.findByIdOrNull(id) ?: throw TicketNotFoundException("Ticket with id $id not found!")
        val auth = SecurityContextHolder.getContext().authentication
        val jwt = SecurityContextHolder.getContext().authentication.principal as Jwt
        val userEmail = jwt.getClaim("email") as String
        val role = auth.authorities.find { it.authority.equals("ROLE_Expert") || it.authority.equals("ROLE_Client")}
        if (role?.authority=="ROLE_Expert") {
            if(ticket.actualExpert?.email != userEmail){
                throw AuthorizationServiceException("The expert logged in is not the expert assigned to ticket")
            }
        }else{
            if(userEmail!=ticket.customer?.email){
                throw AuthorizationServiceException("The customer logged in is not the customer who has opened the ticket")
            }
        }

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
        else throw StateNotValidException("Invalid Request: The ticket is in state ${oldStateDTO?.name}")
    }

    /**
     * Method to close a ticket
     */
    @PreAuthorize("hasAnyRole('ROLE_Manager', 'ROLE_Expert', 'ROLE_Client')")
    @Transactional
    override fun closeIssue(id: Long): TicketDTO? {
        val ticket = ticketRepository.findByIdOrNull(id) ?: throw TicketNotFoundException("Ticket with id $id not found!")
        val jwt = SecurityContextHolder.getContext().authentication.principal as Jwt
        val userEmail = jwt.getClaim("email") as String
        val auth = SecurityContextHolder.getContext().authentication
        val role = auth.authorities.find { it.authority.equals("ROLE_Expert") || it.authority.equals("ROLE_Client")}
        if (role?.authority=="ROLE_Expert") {
            if(ticket.actualExpert?.email != userEmail){
                throw AuthorizationServiceException("The expert logged in is not the expert assigned to ticket")
            }
        }else if (role?.authority=="ROLE_Client"){
            if(userEmail!=ticket.customer?.email){
                throw AuthorizationServiceException("The customer logged in is not the customer who has opened the ticket")
            }
        }
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
        else throw StateNotValidException("Invalid Request: The ticket is in state ${oldStateDTO?.name}")
    }
}