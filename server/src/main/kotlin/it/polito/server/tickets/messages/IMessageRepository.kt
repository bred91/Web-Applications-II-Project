package it.polito.server.tickets.messages

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface IMessageRepository : JpaRepository<Message, Long> {
}