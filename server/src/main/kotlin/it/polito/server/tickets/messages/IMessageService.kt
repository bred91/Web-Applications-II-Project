package it.polito.server.tickets.messages

import org.springframework.web.multipart.MultipartFile

interface IMessageService {
    fun getAllMessages(ticketId: Long): List<MessageDTO>
    fun createMessage(file: MultipartFile?, text: String?, ticketId: Long) : MessageDTO
}