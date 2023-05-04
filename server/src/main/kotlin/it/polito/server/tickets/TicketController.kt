package it.polito.server.tickets


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

    @PostMapping("/API/tickets/createIssue")
    @ResponseStatus(HttpStatus.CREATED)
    fun createIssue(@Valid @RequestBody customer: ProfileDTO, @RequestParam purchaseId: Long ) : TicketDTO? {
        return ticketService.createTicket(customer.email, purchaseId)
    }


    @PutMapping("/API/tickets/startProgress/{id}")
    fun startProgress(@PathVariable id: Long, @Valid @RequestBody startProgressRequestDTO: StartProgressRequestDTO): TicketDTO? {
        return ticketService.startProgress(id, startProgressRequestDTO.employee_id, startProgressRequestDTO.priority_level)
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