package it.polito.server.tickets

interface IMessageService {
    fun getAll(): List<MessageDTO>
    fun getMessage(id: Long): MessageDTO?
    fun createMessage(message: MessageDTO)
    fun updateMessage(id: Long, message: MessageDTO): MessageDTO?
}