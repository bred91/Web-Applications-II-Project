package it.polito.server.tickets


import io.micrometer.observation.annotation.Observed
import it.polito.server.profiles.ProfileDTO
import jakarta.validation.Valid
import lombok.extern.slf4j.Slf4j
import org.hibernate.query.sqm.tree.SqmNode.log
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@Slf4j
@Observed
class TicketController(private val ticketService: ITicketService) {

    @GetMapping("/API/tickets/{id}")
    fun getTicket(@PathVariable id: Long):TicketDTO? {
        return ticketService.getTicketById(id)
    }

    @GetMapping("/API/tickets")
    fun getTickets():List<TicketDTO> {
        // We left this log just to test the custom log message functionality
        log.info("Get tickets")
        return ticketService.getTickets()
    }


    /*@PostMapping("/API/tickets/createIssue")
    @ResponseStatus(HttpStatus.CREATED)
    fun createIssue(@Valid @RequestBody customer: ProfileDTO, @RequestParam purchaseId: Long ) : TicketDTO? {
        return ticketService.createTicket(customer.email, purchaseId)
    }*/

    @PostMapping("/API/tickets/createIssue")
    @ResponseStatus(HttpStatus.CREATED)
    fun createIssue(@RequestParam purchaseId: Long ) : TicketDTO? {
        return ticketService.createTicket(purchaseId)
    }


    @PutMapping("/API/tickets/startProgress/{id}")
    fun startProgress(@PathVariable id: Long, @Valid @RequestBody startProgressRequestDTO: StartProgressRequestDTO): TicketDTO? {
        return ticketService.startProgress(id, startProgressRequestDTO.employee_id, startProgressRequestDTO.priorityLevel.id)
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