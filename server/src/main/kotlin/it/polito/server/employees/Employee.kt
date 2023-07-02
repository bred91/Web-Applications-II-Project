package it.polito.server.employees

import it.polito.server.base.EntityBase
import it.polito.server.tickets.Ticket
import jakarta.persistence.*
@Entity
class Employee : EntityBase<Long>(){
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(updatable = false, nullable = false)
    var id: Long? = null

    var name: String = ""
    var surname: String = ""
    var email: String = ""
    var username: String = ""

    @ManyToOne
    var role: Role? = null

    @OneToMany(mappedBy = "actualExpert")
    var tickets: List<Ticket>? = null

    /*@OneToMany(mappedBy = "expert")
    var states: List<History>? = null*/
}