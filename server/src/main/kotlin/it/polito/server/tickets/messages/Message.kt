package it.polito.server.tickets.messages


import it.polito.server.base.EntityBase
import it.polito.server.employees.Employee
import it.polito.server.tickets.attachments.Attachment
import jakarta.persistence.*
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*

@Document("chat")
class Message {

    @Id
    var id:Long? = null
    var sentTS: Date? = null
    var content:String = ""
    var senderId:String = ""
    var ticketId:Long? = null
}








//@Entity
//class Message : EntityBase<Long>(){
//    @Id
//    @GeneratedValue(strategy = GenerationType.SEQUENCE)
//    @Column(updatable = false, nullable = false)
//    var id: Long? = null
//
//    @Temporal(TemporalType.TIMESTAMP)
//    var sentTS: Date? = null
//
//    var text: String = ""
//    var isSenderCustomer: Boolean = false
//
//    @ManyToOne
//    var expert: Employee? = null
//
//    /*@ManyToOne
//    var ticket: Ticket? = null*/
//
//    @OneToMany(mappedBy = "id")
//    var attachments: List<Attachment>? = null
//}