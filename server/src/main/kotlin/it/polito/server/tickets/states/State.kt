package it.polito.server.tickets.states

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id

@Entity
class State{
    @Id
    @Column(updatable = false, nullable = false)
    var id: Long = -1

    @Column(updatable = false, nullable = false)
    var name: String = ""

    /*@OneToMany(mappedBy = "state")
    var historyList: MutableSet<History>? = null*/

    /*@OneToMany(mappedBy = "state")
    var ticketList: MutableSet<Ticket>? = null*/
}