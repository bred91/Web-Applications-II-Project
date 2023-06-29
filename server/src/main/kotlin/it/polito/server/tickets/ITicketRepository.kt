package it.polito.server.tickets

import it.polito.server.employees.performance.StateCount
import jakarta.persistence.SqlResultSetMapping
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.lang.annotation.Native
import java.time.LocalDateTime
import java.util.*

@Repository
interface ITicketRepository : JpaRepository<Ticket, Long> {
    fun findByActualExpertId(actualExpertId: Long):List<Ticket>

    fun findByCustomerEmail(customerEmail : String) : List<Ticket>

    fun findByActualExpert_Email(email: String): List<Ticket>

    @Query("select s.name as state, count(t.id) as count " +
            "from state s " +
            "left join (select * from ticket t where t.last_modification > :fromDate) " +
            "t on s.id = t.state_id " +
            "group by s.id",
        nativeQuery = true)
    fun getStateCountFromDate(fromDate: Date): List<Any>

    @Query("select " +
            "(select count(*) as ticketsClosed " +
            "from ticket t " +
            "where creation_date > :fromDate) as ticketsCreated, " +
            "(select count(*) as ticketsClosed " +
            "from ticket t " +
            "inner join history h on t.id = h.ticket_id " +
            "inner join state s on h.state_id = s.id " +
            "where h.timestamp > :fromDate and s.name = 'CLOSED') as ticketsClosed, " +
            "(select count(*) as ticketsResolved " +
            "from ticket t " +
            "inner join history h on t.id = h.ticket_id " +
            "inner join state s on h.state_id = s.id " +
            "where h.timestamp > :fromDate and s.name = 'RESOLVED') as ticketsResolved, " +
            "(select count(*) as ticketsResolved " +
            "from ticket t " +
            "inner join history h on t.id = h.ticket_id " +
            "inner join state s on h.state_id = s.id " +
            "where h.timestamp > :fromDate and s.name = 'REOPENED') as ticketsReopened",
            nativeQuery = true)
    fun getTicketsCounters(fromDate: Date): Any

    @Query("select (select count(*) as ticketsClosed " +
            "from ticket t " +
            "inner join state s on t.state_id = s.id " +
            "where t.last_modification > :fromDate and s.name NOT IN ('CLOSED','RESOLVED')) as ticketsWorking",
            nativeQuery = true)
    fun getTicketsWorking(fromDate: Date): Any
}