package it.polito.server.tickets

import it.polito.server.base.EntityBase
import it.polito.server.employees.Employee
import it.polito.server.products.Purchase
import it.polito.server.profiles.Profile
import it.polito.server.tickets.history.History
import it.polito.server.tickets.messages.Message
import it.polito.server.tickets.priorities.Priority
import it.polito.server.tickets.states.State
import jakarta.persistence.*
import java.util.*

@Entity
//@Index(name = "idx_ticket", columnList = "customer_email")
class Ticket : EntityBase<Long>() {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(updatable = false, nullable = false)
    var id: Long? = null

    @Temporal(TemporalType.DATE)
    var creationDate: Date? = null
    @Temporal(TemporalType.TIMESTAMP)
    var lastModification: Date? = null

    @ManyToOne
    var priority: Priority? = null

    @ManyToOne
    var purchase: Purchase? = null

    @ManyToOne
    var state: State? = null

    @ManyToOne
    var customer: Profile? = null

    @ManyToOne
    var actualExpert: Employee? = null

//    @OneToMany(mappedBy = "id")
//    var chat: List<Message>? = null

    @OneToMany(mappedBy = "ticket", cascade = [CascadeType.ALL])
    var history: MutableList<History>? = null
}


fun Ticket.addHistory(history: History) : Ticket {
    if (this.history == null)
        this.history = mutableListOf()

    //val historyEntity = history.toEntity()
    history.ticket = this
    this.history?.add(history)
    return this
}