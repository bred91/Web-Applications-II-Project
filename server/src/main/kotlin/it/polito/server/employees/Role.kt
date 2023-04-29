package it.polito.server.employees

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id

@Entity
class Role {
    @Id
    @Column(updatable = false, nullable = false)
    var id: Long? = null

    @Column(updatable = false, nullable = false)
    var name: String = ""

    /*@OneToMany(mappedBy = "role")
    var employees: MutableSet<Employee>? = null*/
}