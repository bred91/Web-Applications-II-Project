package it.polito.server.tickets.attachments

import it.polito.server.base.EntityBase
import it.polito.server.tickets.messages.Message
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
    var binaryString: String = ""

    @ManyToOne
    var message: Message? = null
}
