package it.polito.server.tickets.messages

import it.polito.server.profiles.ProfileService
import it.polito.server.tickets.ITicketRepository
import it.polito.server.tickets.TicketService
import it.polito.server.tickets.exception.AuthorizationServiceException
import it.polito.server.tickets.exception.TicketNotFoundException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import it.polito.server.tickets.messages.IMessageRepository
import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.web.multipart.MultipartFile
import java.util.*

@Service
class MessageService(private val messageRepository: IMessageRepository,
    private val ticketRepository: ITicketRepository) : IMessageService {


    @PreAuthorize("hasAnyRole('ROLE_Client', 'ROLE_Expert', 'ROLE_Manager')")
    override fun getAllMessages(ticketId:Long): List<MessageDTO> {
        val ticket = ticketRepository.findByIdOrNull(ticketId) ?: throw TicketNotFoundException("Ticket with id $ticketId not found!")

        val jwt = SecurityContextHolder.getContext().authentication.principal as Jwt
        val auth = SecurityContextHolder.getContext().authentication
        val userEmail = jwt.getClaim("email") as String
        if (auth != null && auth.authorities.any { it.authority.equals("ROLE_Expert")} && ticket.actualExpert?.email != userEmail) {
            throw AuthorizationServiceException("The expert logged in is not the expert assigned to ticket")
        }else if(auth != null && auth.authorities.any { it.authority.equals("ROLE_Client")} && ticket.customer?.email != userEmail) {
            throw AuthorizationServiceException("The customer logged in is not the customer who has opened the ticket")
        }
        return messageRepository.getAllMessagesByTicketId(ticketId).map { it.toDTO() }
    }


    @PreAuthorize("hasAnyRole('ROLE_Client', 'ROLE_Expert')")
    override fun createMessage(file: MultipartFile?, text: String?, ticketId: Long) : MessageDTO{

        val ticket = ticketRepository.findByIdOrNull(ticketId) ?: throw TicketNotFoundException("Ticket with id $ticketId not found!")


        val jwt = SecurityContextHolder.getContext().authentication.principal as Jwt
        val auth = SecurityContextHolder.getContext().authentication
        val userEmail = jwt.getClaim("email") as String
        if (auth != null && auth.authorities.any { it.authority.equals("ROLE_Expert")} && ticket.actualExpert?.email != userEmail) {
            throw AuthorizationServiceException("The expert logged in is not the expert assigned to ticket")
        }else if(auth != null && auth.authorities.any { it.authority.equals("ROLE_Client")} && ticket.customer?.email != userEmail) {
            throw AuthorizationServiceException("The customer logged in is not the customer who has opened the ticket")
        }

        
        val attachmentDTO = AttachmentDTO(file?.bytes, file?.originalFilename, file?.contentType)
        val contentDTO = ContentDTO(text, attachmentDTO)

        val messageDTO = MessageDTO(Date(), contentDTO,userEmail, ticketId )
        return messageRepository.save(messageDTO.toEntity()).toDTO()
    }


}