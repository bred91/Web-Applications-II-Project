package it.polito.server.tickets

import it.polito.server.base.EntityBase
import jakarta.persistence.*

@Entity
class Attachment : EntityBase<Long>(){
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(updatable = false, nullable = false)
    var id: Long? = null

    var name: String = ""
    var type: String = ""
    var binary: String = ""

    @ManyToOne
    var message: Message? = null
}
