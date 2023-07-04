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

    @Query(
        /*"SELECT" +
                "(SELECT " +
                "  CASE " +
                "    WHEN (SELECT COUNT(*) FROM ticket) = 0 THEN 0.00" +
                "    ELSE CAST(100.0 * COUNT(*) / (SELECT COUNT(*) FROM ticket) AS numeric(10, 2))" +
                "  END AS closed_perc_week" +
                " FROM (SELECT * " +
                            "FROM ticket t " +
                            "INNER JOIN history h ON t.id = h.ticket_id " +
                            "INNER JOIN state s ON h.state_id = s.id " +
                            "INNER JOIN state s2 ON t.state_id = s2.id " +
                            "WHERE s.name = 'CLOSED' AND s2.name = 'CLOSED' " +
                                "AND CAST(h.timestamp AS DATE) - t.creation_date < 7" +
                            " ) closed_tickets) AS closed_perc_week," +
                " (SELECT CAST(100.0 * COUNT(*) / (SELECT COUNT(*)" +
                            "FROM ticket ) AS numeric(10, 2)) AS resolved_perc_week" +
                            " FROM (SELECT *" +
                            "FROM ticket t " +
                            "INNER JOIN history h ON t.id = h.ticket_id " +
                            "INNER JOIN state s ON h.state_id = s.id " +
                            "INNER JOIN state s2 ON t.state_id = s2.id " +
                            "WHERE s.name = 'RESOLVED' AND s2.name = 'CLOSED' " +
                                "AND CAST(h.timestamp AS DATE) - t.creation_date < 7" +
                            " ) resolved_tickets) AS resolved_perc_week"*/
        """
        SELECT
            (SELECT
                CASE
                    WHEN (SELECT COUNT(*) FROM ticket) = 0 THEN 0.00
                    ELSE CAST(100.0 * COUNT(*) / (SELECT COUNT(*) FROM ticket) AS numeric(10, 2))
                END AS closed_perc_week
            FROM
                (SELECT *
                FROM (
                    SELECT
                        t.*,
                        h.*,
                        ROW_NUMBER() OVER (PARTITION BY t.id ORDER BY h.timestamp DESC) AS row_number
                    FROM ticket t
                    INNER JOIN history h ON t.id = h.ticket_id
                    INNER JOIN state s ON h.state_id = s.id
                    INNER JOIN state s2 on s2.id = t.state_id
                    WHERE s.name = 'CLOSED' AND s2.name = 'CLOSED' 
                        AND CAST(h.timestamp AS DATE) - t.creation_date < 7
                ) AS ticket_history
                WHERE ticket_history.row_number = 1) closed_tickets) AS closed_perc_week,
            (SELECT CAST(100.0 * COUNT(*) / (SELECT COUNT(*) FROM ticket) AS numeric(10, 2)) AS resolved_perc_week
            FROM
                (SELECT *
                FROM (
                    SELECT
                        t.*,
                        h.*,
                        ROW_NUMBER() OVER (PARTITION BY t.id ORDER BY h.timestamp DESC) AS row_number
                    FROM ticket t
                    INNER JOIN history h ON t.id = h.ticket_id
                    INNER JOIN state s ON h.state_id = s.id
                    INNER JOIN state s2 on s2.id = t.state_id
                    WHERE s.name = 'RESOLVED' AND s2.name IN ('CLOSED', 'RESOLVED')
                        AND CAST(h.timestamp AS DATE) - t.creation_date < 7
                ) AS ticket_history
                WHERE ticket_history.row_number = 1) resolved_tickets) AS resolved_perc_week
        """
        ,
        nativeQuery = true
    )
    fun getPercentageCounters(): Any

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

    @Query(
        /*"SELECT " +
                "(SELECT CASE " +
                "    WHEN (SELECT COUNT(*) FROM ticket t WHERE t.actual_expert_id = :expertId) = 0 THEN 0.00 " +
                "    ELSE CAST(100.0 * COUNT(*) / (SELECT COUNT(*) FROM ticket t WHERE t.actual_expert_id = :expertId) AS numeric(10, 2))" +
                "  END AS closed_perc_week" +
                " FROM (SELECT * " +
                        "FROM ticket t " +
                        "INNER JOIN history h ON t.id = h.ticket_id " +
                        "INNER JOIN state s ON h.state_id = s.id " +
                        "WHERE s.name = 'RESOLVED'" +
                        "AND CAST(h.timestamp AS DATE) - t.creation_date < 7" +
                        " AND t.actual_expert_id = :expertId) as closed_tickets) AS closed_perc_week," +
                "(SELECT CASE " +
                "    WHEN (SELECT COUNT(*) FROM ticket t WHERE t.actual_expert_id = :expertId) = 0 THEN 0.00 " +
                "    ELSE CAST(100.0 * COUNT(*) / (SELECT COUNT(*) FROM ticket t WHERE t.actual_expert_id = :expertId) AS numeric(10, 2))" +
                "  END AS resolved_perc_week" +
                " FROM (SELECT *" +
                        "FROM ticket t " +
                        "INNER JOIN history h ON t.id = h.ticket_id " +
                        "INNER JOIN state s ON h.state_id = s.id " +
                        "WHERE s.name = 'RESOLVED'" +
                        "AND CAST(h.timestamp AS DATE) - t.creation_date < 7" +
                        " AND t.actual_expert_id = :expertId) as resolved_tickets) AS resolved_perc_week"*/
        """
        SELECT
            (SELECT
                CASE
                    WHEN (SELECT COUNT(DISTINCT t.id) FROM ticket t
                                INNER JOIN history h on t.id = h.ticket_id
                                WHERE h.expert_id = :expertId) = 0 THEN 0.00
                    ELSE CAST(100.0 * COUNT(*) / (SELECT COUNT(DISTINCT t.id) FROM ticket t
                                                    INNER JOIN history h on t.id = h.ticket_id
                                                    WHERE h.expert_id = :expertId) AS numeric(10, 2))
                END AS closed_perc_week
            FROM
                (SELECT *
                FROM (
                    SELECT
                        t.*,
                        h.*,
                        ROW_NUMBER() OVER (PARTITION BY t.id ORDER BY h.timestamp DESC) AS row_number
                    FROM ticket t
                    INNER JOIN history h ON t.id = h.ticket_id
                    INNER JOIN state s ON h.state_id = s.id
                    INNER JOIN state s2 on s2.id = t.state_id
                    WHERE s.name = 'CLOSED' AND s2.name = 'CLOSED'
                        AND CAST(h.timestamp AS DATE) - t.creation_date < 7 AND t.actual_expert_id = :expertId
                ) AS ticket_history
                WHERE ticket_history.row_number = 1) closed_tickets) AS closed_perc_week,
            (SELECT
                CASE
                    WHEN (SELECT COUNT(DISTINCT t.id) FROM ticket t
                            INNER JOIN history h on t.id = h.ticket_id
                            WHERE h.expert_id = :expertId) = 0 THEN 0.00
                    ELSE CAST(100.0 * COUNT(*) / (SELECT COUNT(DISTINCT t.id) FROM ticket t
                                                    INNER JOIN history h on t.id = h.ticket_id
                                                    WHERE h.expert_id = :expertId) AS numeric(10, 2))
                END AS resolved_perc_week
            FROM
                (SELECT *
                FROM (
                    SELECT
                        t.*,
                        h.*,
                        ROW_NUMBER() OVER (PARTITION BY t.id ORDER BY h.timestamp DESC) AS row_number
                    FROM ticket t
                    INNER JOIN history h ON t.id = h.ticket_id
                    INNER JOIN state s ON h.state_id = s.id
                    INNER JOIN state s2 on s2.id = t.state_id
                    WHERE s.name = 'RESOLVED' AND s2.name IN ('RESOLVED', 'CLOSED')
                        AND CAST(h.timestamp AS DATE) - t.creation_date < 7 AND t.actual_expert_id = :expertId
                ) AS ticket_history
                WHERE ticket_history.row_number = 1) resolved_tickets) AS resolved_perc_week
        """
        ,
        nativeQuery = true
    )
    fun getPercentageCounters(expertId: Long): Any
}