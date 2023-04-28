package it.polito.server.tickets

import it.polito.server.base.EntityBase
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.ManyToOne

@Entity
class Attachment : EntityBase<Long>(){
    @Id
    @Column(updatable = false, nullable = false)
    var id: Long? = null

    var name: String = ""
    var type: String = ""
    var binary: String = ""

    @ManyToOne
    var message: Message? = null
}
