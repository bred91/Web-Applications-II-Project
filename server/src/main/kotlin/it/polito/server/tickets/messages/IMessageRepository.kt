package it.polito.server.tickets.messages

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface IMessageRepository :MongoRepository<Message, Long> {
    fun getAllMessagesByTicketId(ticketId:Long):List<Message>
}