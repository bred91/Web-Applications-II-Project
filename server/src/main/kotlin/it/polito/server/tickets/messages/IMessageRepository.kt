package it.polito.server.tickets.messages

import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository

interface IMessageRepository : MongoRepository<Message, ObjectId> {
    fun getAllMessagesByTicketId(ticketId:Long):List<Message>
}