package it.polito.server.tickets.messages

import jakarta.persistence.*
import org.bson.types.ObjectId
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*

@Document("attachments")
class Attachment {

    @Id
    var id:ObjectId? = null
    var sentTS: Date? = null
    var content:ByteArray? = null
    var senderId:String = ""
    var ticketId:Long? = null
    var filename:String = ""
    var contentType:String = ""
}