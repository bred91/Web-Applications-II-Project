package it.polito.server.employees

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.OneToMany

@Entity
class Role {
    @Id
    @Column(updatable = false, nullable = false)
    var id: Long? = null

    @OneToMany(mappedBy = "role")
    var name: MutableSet<Employee>? = null
}