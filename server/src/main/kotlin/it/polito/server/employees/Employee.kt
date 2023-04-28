package it.polito.server.employees

import it.polito.server.tickets.History
import it.polito.server.tickets.Ticket
import jakarta.persistence.*
@Entity
class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(updatable = false, nullable = false)
    var id: Long? = null

    var name: String = ""
    var surname: String = ""
    var email: String = ""

    @ManyToOne
    var role: Role? = null

    @OneToMany(mappedBy = "actualExpert")
    var tickets: List<Ticket>? = null

    @OneToMany(mappedBy = "expert")
    var states: List<History>? = null
}