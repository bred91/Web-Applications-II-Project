package it.polito.server.tickets

interface ITicketService {
        fun getTickets(): List<TicketDTO>
        fun getTicket(id: Long): TicketDTO?
        fun createTicket(ticket: JustTicketDTO)
        fun updateTicket(id: Long, ticket: TicketDTO): TicketDTO?

        fun startProgress(id: Long, ticket: JustTicketDTO): TicketDTO?
        /*fun deleteTicket(id: Long)*/
        /*fun getHistory(id: Long): List<HistoryDTO>
        fun addHistory(id: Long, history: HistoryDTO)
        fun getAttachments(id: Long): List<AttachmentDTO>
        fun addAttachment(id: Long, attachment: AttachmentDTO)
        fun getStates(): List<StateDTO>
        fun getState(id: String): StateDTO?
        fun createState(state: StateDTO)
        fun updateState(id: String, state: StateDTO): StateDTO?*/
        /*fun deleteState(id: String)*/
}