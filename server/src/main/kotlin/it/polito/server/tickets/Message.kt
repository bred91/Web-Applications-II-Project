package it.polito.server.tickets


import it.polito.server.base.EntityBase
import it.polito.server.employees.Employee
import jakarta.persistence.*
import java.util.*

@Entity
class Message : EntityBase<Long>(){
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(updatable = false, nullable = false)
    var id: Long? = null

    @Temporal(TemporalType.TIMESTAMP)
    var sentTS: Date? = null

    var text: String = ""
    var isSenderCustomer: Boolean = false

    @ManyToOne
    var expert: Employee? = null

    /*@ManyToOne
    var ticket: Ticket? = null*/

    @OneToMany(mappedBy = "id")
    var attachments: List<Attachment>? = null
}