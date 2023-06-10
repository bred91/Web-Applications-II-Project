package it.polito.server.tickets.messages

import org.springframework.data.repository.findByIdOrNull

class MessageService(private val messageRepository: IMessageRepository) : IMessageService {
    override fun getAllMessages(ticketId:Long): List<MessageDTO> {
        return messageRepository.getAllMessagesByTicketId(ticketId).map { it.toDTO() }
    }

    override fun getMessage(id: Long): MessageDTO? {
        return messageRepository.findByIdOrNull(id)?.toDTO()
    }

    override fun createMessage(message: MessageDTO) {
        messageRepository.save(message.toEntity())
    }

    override fun updateMessage(id: Long, message: MessageDTO): MessageDTO? {
        TODO("Not yet implemented")
    }

}