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

    var name: String = ""

    @OneToMany(mappedBy = "role")
    var employees: MutableSet<Employee>? = null
}