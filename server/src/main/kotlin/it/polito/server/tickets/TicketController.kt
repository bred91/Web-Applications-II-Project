package it.polito.server.tickets

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
        return ticketService.getTicket(id)
    }

    @PostMapping("/API/tickets/")
    @ResponseStatus(HttpStatus.CREATED)
    fun createIssue(@Valid @RequestBody ticket: TicketDTO) {
        ticketService.createTicket(ticket)
    }

    @PutMapping("/API/tickets/{id}")
    fun startProgress(@PathVariable id: Long, @Valid @RequestBody ticket: TicketDTO):TicketDTO? {
        return ticketService.startProgress(id, ticket)
    }

    /*@GetMapping("/API/tickets/{id}/history")
    fun getHistory(id: Long):List<HistoryDTO> {
        return ticketService.getHistory(id)
    }*/
}