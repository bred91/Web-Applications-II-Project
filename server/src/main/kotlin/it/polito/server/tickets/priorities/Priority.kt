package it.polito.server.tickets.priorities

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id

@Entity
class Priority {
    @Id
    @Column(updatable = false, nullable = false)
    var id : Long = -1
    @Column(updatable = false, nullable = false)
    var name: String= ""
}