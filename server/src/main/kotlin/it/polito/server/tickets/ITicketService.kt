package it.polito.server.tickets

interface ITicketService {
        fun getTickets(): List<TicketDTO>
        fun getTicketById(id: Long): TicketDTO?
        fun createTicket(ticketDTO: TicketToSaveDTO)
        fun startProgress(id: Long, ticketDTO: TicketToSaveDTO): TicketDTO?
        fun stopProgress(id: Long, ticketDTO: TicketToSaveDTO): TicketDTO?
        fun resolveIssue(id: Long, ticketDTO: TicketToSaveDTO): TicketDTO?
        fun closeIssue(id: Long, ticketDTO: TicketToSaveDTO): TicketDTO?
        fun reopenIssue(id: Long, ticketDTO: TicketToSaveDTO): TicketDTO?
}