package it.polito.server.tickets.history
import it.polito.server.base.EntityBase
import it.polito.server.employees.Employee
import it.polito.server.tickets.states.State
import it.polito.server.tickets.Ticket
import jakarta.persistence.*
import java.util.*

@Entity
class History : EntityBase<Long>(){
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(updatable = false, nullable = false)
    var id: Long? = null

    @ManyToOne
    var state: State? = null

    @ManyToOne(fetch = FetchType.LAZY)
    var ticket: Ticket? = null

    @Temporal(TemporalType.TIMESTAMP)
    var timestamp: Date? = null

    @ManyToOne
    var expert: Employee? = null
}