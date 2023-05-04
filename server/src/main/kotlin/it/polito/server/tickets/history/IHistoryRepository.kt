package it.polito.server.tickets.history

import it.polito.server.tickets.history.History
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface IHistoryRepository : JpaRepository<History, Long> {
}