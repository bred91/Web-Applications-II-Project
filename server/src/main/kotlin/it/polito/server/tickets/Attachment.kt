package it.polito.server.tickets

import it.polito.server.base.EntityBase
import jakarta.persistence.*

@Entity
class Attachment : EntityBase<Long>(){
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(updatable = false, nullable = false)
    var id: Long? = null

    var name: String = ""
    var type: String = ""
    // NOTE: we decide to temporally save them as binary data
    // a better solution would be to save them in NoSQL DBMS, like MongoDB
    var binary_string: String = ""

    @ManyToOne
    var message: Message? = null
}
