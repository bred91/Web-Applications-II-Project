package it.polito.server.tickets

import it.polito.server.base.EntityBase
import it.polito.server.employees.Employee
import it.polito.server.profiles.Profile
import jakarta.persistence.*
import java.util.*

@Entity
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
    var state: State? = null

    @ManyToOne
    var customer: Profile? = null

    @ManyToOne
    var actualExpert: Employee? = null

    @OneToMany(mappedBy = "id")
    var chat: List<Message>? = null

    @OneToMany(mappedBy = "ticket")
    var history: List<History>? = null
}