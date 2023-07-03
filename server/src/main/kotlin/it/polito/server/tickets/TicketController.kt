package it.polito.server.tickets


import io.micrometer.observation.annotation.Observed
import it.polito.server.profiles.ProfileDTO
import jakarta.validation.Valid
import lombok.extern.slf4j.Slf4j
import org.hibernate.query.sqm.tree.SqmNode.log
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.web.bind.annotation.*

@RestController
@Slf4j
@Observed
class TicketController(private val ticketService: ITicketService) {

    @Autowired
    private val messagingTemplate: SimpMessagingTemplate? = null

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


    @PostMapping("/API/tickets/createIssue")
    @ResponseStatus(HttpStatus.CREATED)
    fun createIssue(@RequestParam purchaseId: Long ) : TicketDTO? {
        var ticket =  ticketService.createTicket(purchaseId);
        if(ticket!=null){
            messagingTemplate?.convertAndSendToUser("manager", "/tickets",ticket)
            ticket.customer?.let { messagingTemplate?.convertAndSendToUser(it.email, "/tickets",ticket) }
        }
        return ticket;
    }




    @PutMapping("/API/tickets/startProgress/{id}")
    fun startProgress(@PathVariable id: Long, @Valid @RequestBody startProgressRequestDTO: StartProgressRequestDTO): TicketDTO? {
        var ticketPrev = ticketService.getTicketById(id);
        var ticketUpdated =  ticketService.startProgress(id, startProgressRequestDTO.employee_id, startProgressRequestDTO.priorityLevel.id)
        if (ticketUpdated != null) {
            ticketUpdated.customer?.let { messagingTemplate?.convertAndSendToUser(it.email, "/tickets",ticketUpdated) }
            ticketUpdated.actualExpert?.let { messagingTemplate?.convertAndSendToUser(it.email, "/tickets",ticketUpdated) }
            messagingTemplate?.convertAndSendToUser("manager", "/tickets",ticketUpdated)
            if(ticketPrev != null){
                ticketPrev.actualExpert?.let { messagingTemplate?.convertAndSendToUser(it.email, "/tickets", ticketUpdated) }
            }
        }
        return ticketUpdated

    }

    @PutMapping("/API/tickets/stopProgress/{id}")
    fun stopProgress(@PathVariable id:Long):TicketDTO? {
        var ticketPrev = ticketService.getTicketById(id);
        var ticketUpdated = ticketService.stopProgress(id)
        if (ticketUpdated != null) {
            ticketUpdated.actualExpert?.let { messagingTemplate?.convertAndSendToUser(it.email, "/tickets",ticketUpdated) }
            ticketUpdated.customer?.let { messagingTemplate?.convertAndSendToUser(it.email, "/tickets",ticketUpdated) }
            messagingTemplate?.convertAndSendToUser("manager", "/tickets",ticketUpdated)
            if(ticketPrev != null){
                ticketPrev.actualExpert?.let { messagingTemplate?.convertAndSendToUser(it.email, "/tickets", ticketUpdated) }
            }
        }

        return ticketUpdated
    }

    @PutMapping("/API/tickets/reopenIssue/{id}")
    fun reopenIssue(@PathVariable id:Long):TicketDTO? {
        var ticketUpdated = ticketService.reopenIssue(id)
        if (ticketUpdated != null) {
            ticketUpdated.actualExpert?.let { messagingTemplate?.convertAndSendToUser(it.email, "/tickets",ticketUpdated) }
            ticketUpdated.customer?.let { messagingTemplate?.convertAndSendToUser(it.email, "/tickets",ticketUpdated) }
            messagingTemplate?.convertAndSendToUser("manager", "/tickets",ticketUpdated)
        }
        return ticketUpdated
    }

    @PutMapping("/API/tickets/resolveIssue/{id}")
    fun resolveIssue(@PathVariable id:Long):TicketDTO? {
        var ticketUpdated = ticketService.resolveIssue(id)
        if (ticketUpdated != null) {
            ticketUpdated.actualExpert?.let { messagingTemplate?.convertAndSendToUser(it.email, "/tickets",ticketUpdated) }
            ticketUpdated.customer?.let { messagingTemplate?.convertAndSendToUser(it.email, "/tickets",ticketUpdated) }
            messagingTemplate?.convertAndSendToUser("manager", "/tickets",ticketUpdated)
        }
        return ticketUpdated
    }

    @PutMapping("/API/tickets/closeIssue/{id}")
    fun closeIssue(@PathVariable id:Long):TicketDTO? {
        var ticketUpdated =  ticketService.closeIssue(id)
        if (ticketUpdated != null) {
            ticketUpdated.actualExpert?.let { messagingTemplate?.convertAndSendToUser(it.email, "/tickets",ticketUpdated) }
            ticketUpdated.customer?.let { messagingTemplate?.convertAndSendToUser(it.email, "/tickets",ticketUpdated) }
            messagingTemplate?.convertAndSendToUser("manager", "/tickets",ticketUpdated)
        }
        return ticketUpdated
    }
}