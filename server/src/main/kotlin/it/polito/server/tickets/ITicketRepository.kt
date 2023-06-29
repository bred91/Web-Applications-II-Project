package it.polito.server.tickets

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface ITicketRepository : JpaRepository<Ticket, Long> {
    fun findByActualExpertId(actualExpertId: Long):List<Ticket>

    fun findByCustomerEmail(customerEmail : String) : List<Ticket>

    fun findByActualExpert_Email(email: String): List<Ticket>

    @Query("select s.name as state, count(t.id) as count " +
            "from state s " +
            "left join ticket t " +
            "on s.id = t.state_id " +
            "group by s.id " +
            "order by s.id",
        nativeQuery = true)
    fun getStateCountFromDate(): List<Any>

    @Query("select " +
            "(select count(*) as ticketsCreated " +
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
            "(select count(*) as ticketsReopened " +
            "from ticket t " +
            "inner join history h on t.id = h.ticket_id " +
            "inner join state s on h.state_id = s.id " +
            "where h.timestamp > :fromDate and s.name = 'REOPENED') as ticketsReopened",
            nativeQuery = true)
    fun getTicketsCounters(fromDate: Date): Any

    @Query("select s.name as state, count(t.id) as count " +
            "from state s " +
            "left join (select * from ticket t where t.actual_expert_id = :expertId) " +
            "t on s.id = t.state_id " +
            "group by s.id " +
            "order by s.id",
        nativeQuery = true)
    fun getStateCountFromDate(expertId: Long): List<Any>

    @Query("select " +
            "(select count(*) as ticketsInProgress " +
            "from ticket t " +
            "inner join history h on t.id = h.ticket_id " +
            "inner join state s on h.state_id = s.id " +
            "where h.timestamp > :fromDate and s.name = 'IN_PROGRESS' and t.actual_expert_id = :expertId) as ticketsInProgress, " +
            "(select count(*) as ticketsClosed " +
            "from ticket t " +
            "inner join history h on t.id = h.ticket_id " +
            "inner join state s on h.state_id = s.id " +
            "where h.timestamp > :fromDate and s.name = 'CLOSED' and t.actual_expert_id = :expertId) as ticketsClosed, " +
            "(select count(*) as ticketsResolved " +
            "from ticket t " +
            "inner join history h on t.id = h.ticket_id " +
            "inner join state s on h.state_id = s.id " +
            "where h.timestamp > :fromDate and s.name = 'RESOLVED' and t.actual_expert_id = :expertId) as ticketsResolved, " +
            "(select count(*) as ticketsReopened " +
            "from ticket t " +
            "inner join history h on t.id = h.ticket_id " +
            "inner join state s on h.state_id = s.id " +
            "where h.timestamp > :fromDate and s.name = 'REOPENED' and t.actual_expert_id = :expertId) as ticketsReopened",
        nativeQuery = true)
    fun getTicketsCounters(fromDate: Date, expertId: Long): Any
}