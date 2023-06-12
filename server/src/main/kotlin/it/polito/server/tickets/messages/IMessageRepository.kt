package it.polito.server.tickets.messages

import org.springframework.data.mongodb.repository.MongoRepository

interface IMessageRepository : MongoRepository<Message, Long> {
    fun getAllMessagesByTicketId(ticketId:Long):List<Message>
}