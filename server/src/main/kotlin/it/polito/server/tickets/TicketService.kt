package it.polito.server.tickets

import it.polito.server.tickets.exception.TicketNotFoundException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class TicketService (private val ticketRepository: ITicketRepository,
    private val historyRepository: IHistoryRepository) : ITicketService {

    override fun getTickets(): List<TicketDTO> {
        return ticketRepository.findAll().map { it.toDTO() }
    }

    override fun getTicket(id: Long): TicketDTO? {
        return ticketRepository.findByIdOrNull(id)?.toDTO()
            ?: throw TicketNotFoundException("Ticket with id $id not found")
    }

    @Transactional
    override fun createTicket(ticket: TicketDTO) {
        if(ticket.id == null) {
            ticketRepository.save(ticket.toEntity())
        }

    }

    // todo: @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    @Transactional
    override fun startProgress(id: Long, ticket: TicketDTO): TicketDTO? {
        return when (ticketRepository.findByIdOrNull(id)?.toDTO()) {
            null -> {throw TicketNotFoundException("Ticket with id $id not found")}
            else -> {
                if(ticket.state?.equals("OPEN") == true) {
                    throw IllegalStateException("Ticket with id $id is not in OPEN state")
                }
                //TODO("add history save")
                ticketRepository.save(ticket.toEntity()).toDTO()
            }
        }
    }

    override fun updateTicket(id: Long, ticket: TicketDTO): TicketDTO? {
        TODO("Not yet implemented")
    }

    override fun getHistory(id: Long): List<HistoryDTO> {
        TODO("Not yet implemented")
    }

    override fun addHistory(id: Long, history: HistoryDTO) {
        TODO("Not yet implemented")
    }

    override fun getAttachments(id: Long): List<AttachmentDTO> {
        TODO("Not yet implemented")
    }

    override fun addAttachment(id: Long, attachment: AttachmentDTO) {
        TODO("Not yet implemented")
    }

    override fun getStates(): List<StateDTO> {
        TODO("Not yet implemented")
    }

    override fun getState(id: String): StateDTO? {
        TODO("Not yet implemented")
    }

    override fun createState(state: StateDTO) {
        TODO("Not yet implemented")
    }

    override fun updateState(id: String, state: StateDTO): StateDTO? {
        TODO("Not yet implemented")
    }
}