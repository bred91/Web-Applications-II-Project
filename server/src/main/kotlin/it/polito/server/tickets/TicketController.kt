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
        return ticketService.getTicketById(id)
    }

    @PostMapping("/API/tickets/")
    @ResponseStatus(HttpStatus.CREATED)
    fun createIssue(@Valid @RequestBody ticketDTO: TicketToSaveDTO) {
        ticketService.createTicket(ticketDTO)
    }

    @PutMapping("/API/tickets/startProgress/{id}")
    fun startProgress(@PathVariable id: Long, @Valid @RequestBody ticketDTO: TicketToSaveDTO): TicketDTO? {
        return ticketService.startProgress(id, ticketDTO)
    }

    /*-	Stop Progress (M)
    -	Resolve Issue (C & E ?)*/


    /*-	Close Issue (C) T	-> valutare batch (magari per il progetto finale)
    -	Reopen Issue (C)*/
}