package it.polito.server.tickets

import com.fasterxml.jackson.databind.exc.InvalidNullException
import com.fasterxml.jackson.databind.exc.InvalidTypeIdException
import it.polito.server.employees.EmployeeDTO
import it.polito.server.profiles.ProfileDTO
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import java.lang.IllegalArgumentException

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
    fun createIssue(@Valid @RequestBody customer: ProfileDTO) : TicketDTO? {
        return ticketService.createTicket(customer.email)
    }

    /*@PutMapping("/API/tickets/startProgress/{id}")
    fun startProgress(@PathVariable id: Long, @Valid @RequestBody ticketDTO: TicketToSaveDTO): TicketDTO? {
        return ticketService.startProgress(id, ticketDTO)
    }*/
    /*@PutMapping("/API/tickets/startProgress/{id}/{id_employee}")
    fun startProgress(@PathVariable id: Long, @PathVariable id_employee : Long): TicketDTO? {
        return ticketService.startProgress(id, id_employee)
    }*/



    @PutMapping("/API/tickets/startProgress/{id}")
    fun startProgress(@PathVariable id: Long, @Valid @RequestBody employeeDTO: EmployeeDTO): TicketDTO? {
        return ticketService.startProgress(id, employeeDTO.id ?: throw IllegalArgumentException("employee id not present in the body"))
    }

    /*-	Stop Progress (M)
    -	Resolve Issue (C & E ?)*/
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




    /*-	Close Issue (C) T	-> valutare batch (magari per il progetto finale)
    -	Reopen Issue (C)*/
}