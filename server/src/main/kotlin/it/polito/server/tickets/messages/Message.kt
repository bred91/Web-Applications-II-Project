package it.polito.server.tickets.messages

import jakarta.persistence.*
import org.bson.types.ObjectId
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*



@Document("messages")
class Message {

    @Id
    var id:ObjectId? = null
    var sentTS: Date? = null
    var content: Content? = null
    var senderId:String = ""
    var ticketId:Long? = null

}

class Content {
    var text:String? = null
    var attachment:Attachment? = null
}
