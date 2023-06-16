package it.polito.server.tickets.messages

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import it.polito.server.tickets.messages.IMessageRepository

@Service
class MessageService(private val messageRepository: IMessageRepository) : IMessageService {



    override fun getAllMessages(ticketId:Long): List<MessageDTO> {
        println("XXXXXXXXXXXXXXXXXXXXXXXXXXX")
        return messageRepository.getAllMessagesByTicketId(ticketId).map { it.toDTO() }
    }

    override fun getMessage(id: Long): MessageDTO? {
        //return messageRepository.findByIdOrNull(id)?.toDTO()
        TODO()
    }

    override fun createMessage(message: MessageDTO) {
        val entity = message.toEntity()
        messageRepository.save(message.toEntity())
    }

    override fun updateMessage(id: Long, message: MessageDTO): MessageDTO? {
        TODO("Not yet implemented")
    }

}