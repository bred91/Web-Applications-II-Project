package it.polito.server.tickets

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.OneToMany

@Entity
class State{
    @Id
    @Column(updatable = false, nullable = false)
    var id: Long? = null

    var name: String = ""

    @OneToMany(mappedBy = "state")
    var historyList: MutableSet<History>? = null
}