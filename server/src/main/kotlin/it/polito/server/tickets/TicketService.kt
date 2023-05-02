package it.polito.server.tickets

import it.polito.server.tickets.enums.StateEnum
import it.polito.server.tickets.enums.toLong
import it.polito.server.tickets.exception.NullTicketIdException
import it.polito.server.tickets.exception.TicketNotFoundException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class TicketService (private val ticketRepository: ITicketRepository,
    private val stateService: StateService) : ITicketService {

    override fun getTickets(): List<TicketDTO> {
        return ticketRepository.findAll().map { it.toDTO() }
    }

    override fun getTicketById(id: Long): TicketDTO? {
        return ticketRepository.findByIdOrNull(id)?.toDTO()
            ?: throw TicketNotFoundException("Ticket with id $id not found")
    }

    /**
     * From the client we expect a new Ticket with the state field set to NULL
     */
    // todo: @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    @Transactional
    override fun createTicket(ticketDTO: TicketToSaveDTO) {
        if(ticketDTO.id == null) {
            if(ticketDTO.state == null){

                val newState = stateService.getState(StateEnum.OPEN.toLong())

                ticketDTO.state = newState
                val historyDTO = HistoryToSaveDTO(
                    null,
                    newState,
                    null,
                    ticketDTO.lastModification,
                    ticketDTO.actualExpert
                )

                ticketDTO.state = newState

                ticketRepository.save(ticketDTO.toEntity().addHistory(historyDTO))
            }
            else{
                throw IllegalStateException("Ticket state must be null at creation")
            }
        }
        else {
            throw NullTicketIdException("Ticket id must be null")
        }
    }

    /**
        * From the client we expect the same ticket with the state field set to "OPEN"
        * and the actualExpert field set to the expert that will handle the ticket
        * @return the full ticket aggregate, with history and chat
     */
    // todo: @PreAuthorize("hasRole('ROLE_MANAGER')")
    @Transactional
    override fun startProgress(id: Long, ticketDTO: TicketToSaveDTO): TicketDTO? {

        val preTicket = ticketRepository.findByIdOrNull(id) ?: throw TicketNotFoundException("Ticket with id $id not found")

        val oldStateDTO = stateService.getState(StateEnum.OPEN.toLong())
        if (preTicket.state?.id == oldStateDTO!!.id && ticketDTO.state?.id == oldStateDTO.id){
            val newState = stateService.getState(StateEnum.IN_PROGRESS.toLong())

            ticketDTO.state = newState

            val historyDTO = HistoryToSaveDTO(
                null,
                newState!!,
                ticketDTO,
                ticketDTO.lastModification,
                ticketDTO.actualExpert
            )

            ticketRepository.save(ticketDTO.toEntity().addHistory(historyDTO)).toDTO()
            return getTicketById(id)
        }
        else
            throw IllegalStateException("Ticket state must be OPEN")
    }

    override fun stopProgress(id: Long, ticketDTO: TicketToSaveDTO): TicketDTO? {
        TODO("Not yet implemented")
    }

    override fun resolveIssue(id: Long, ticketDTO: TicketToSaveDTO): TicketDTO? {
        TODO("Not yet implemented")
    }

    override fun closeIssue(id: Long, ticketDTO: TicketToSaveDTO): TicketDTO? {
        TODO("Not yet implemented")
    }

    override fun reopenIssue(id: Long, ticketDTO: TicketToSaveDTO): TicketDTO? {
        TODO("Not yet implemented")
    }
}