package it.polito.server.tickets

import org.springframework.web.bind.annotation.PathVariable

interface ITicketService {
        fun getTickets(): List<TicketDTO>
        fun getTicketById(id: Long): TicketDTO?
        fun createTicket(customerEmail: String) : TicketDTO?

        fun startProgress(id_ticket: Long, id_employee:Long ): TicketDTO?


        fun stopProgress(id:Long):TicketDTO?


        fun reopenIssue(id:Long):TicketDTO?

        fun resolveIssue(id:Long):TicketDTO?

        fun closeIssue(id:Long):TicketDTO?
}