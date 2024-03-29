package it.polito.server.tickets

interface ITicketService {
        fun getTickets(): List<TicketDTO>
        fun getTicketById(id: Long): TicketDTO?
        fun createTicket(purchaseId: Long) : TicketDTO?
        fun startProgress(idTicket: Long, idEmployee:Long, priorityLevel: Long ): TicketDTO?

        fun stopProgress(id:Long):TicketDTO?

        fun reopenIssue(id:Long):TicketDTO?

        fun resolveIssue(id:Long):TicketDTO?

        fun closeIssue(id:Long):TicketDTO?
}