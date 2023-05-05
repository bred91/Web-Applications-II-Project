package it.polito.server.tickets.messages

interface IMessageService {
    fun getAllMessages(): List<MessageDTO>
    fun getMessage(id: Long): MessageDTO?
    fun createMessage(message: MessageDTO)
    fun updateMessage(id: Long, message: MessageDTO): MessageDTO?
}