package it.polito.server.tickets
import it.polito.server.base.EntityBase
import it.polito.server.employees.Employee
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

    @ManyToOne
    var ticket: Ticket? = null

    @Temporal(TemporalType.TIMESTAMP)
    var timestamp: Date? = null

    @ManyToOne
    var expert: Employee? = null
}