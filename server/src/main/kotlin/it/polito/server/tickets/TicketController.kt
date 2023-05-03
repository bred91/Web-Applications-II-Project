package it.polito.server.tickets

import it.polito.server.employees.EmployeeDTO
import it.polito.server.profiles.ProfileDTO
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
class TicketController(private val ticketService: ITicketService) {

    @GetMapping("/API/tickets")
    fun getTickets():List<TicketDTO> {
        return ticketService.getTickets()
    }

    @GetMapping("/API/tickets/{id}")
    fun getTicket(@PathVariable id: Long):TicketDTO? {
        return ticketService.getTicketById(id)
    }

    // TODO aggiungere il purchase (id in querystring?)
    @PostMapping("/API/tickets/createIssue")
    @ResponseStatus(HttpStatus.CREATED)
    fun createIssue(@Valid @RequestBody customer: ProfileDTO) : TicketDTO? {
        return ticketService.createTicket(customer.email)
    }

    // TODO:aggiungere @Valid @RequestBody priorityLevel: String
    @PutMapping("/API/tickets/startProgress/{id}")
    fun startProgress(@PathVariable id: Long, @Valid @RequestBody employeeDTO: EmployeeDTO): TicketDTO? {
        return ticketService.startProgress(id, employeeDTO.id ?: throw IllegalArgumentException("employee id not present in the body"), priorityLevel = "low")
    }

    @PutMapping("/API/tickets/stopProgress/{id}")
    fun stopProgress(@PathVariable id:Long):TicketDTO? {
        return ticketService.stopProgress(id)
    }

    @PutMapping("/API/tickets/reopenIssue/{id}")
    fun reopenIssue(@PathVariable id:Long):TicketDTO? {
        return ticketService.reopenIssue(id)
    }

    @PutMapping("/API/tickets/resolveIssue/{id}")
    fun resolveIssue(@PathVariable id:Long):TicketDTO? {
        return ticketService.resolveIssue(id)
    }

    @PutMapping("/API/tickets/closeIssue/{id}")
    fun closeIssue(@PathVariable id:Long):TicketDTO? {
        return ticketService.closeIssue(id)
    }
}