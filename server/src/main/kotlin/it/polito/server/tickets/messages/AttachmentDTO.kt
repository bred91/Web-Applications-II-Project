package it.polito.server.tickets.messages

import jakarta.persistence.Id
import org.bson.types.ObjectId
import java.util.*


data class AttachmentDTO (
    var content:ByteArray?,
    var filename:String?,
    var contentType:String?
)

fun AttachmentDTO.toEntity():Attachment {
    val attachment = Attachment()
    attachment.content = content
    attachment.filename = filename
    attachment.contentType = contentType
    return attachment
}

fun Attachment.toDTO() : AttachmentDTO{
    return AttachmentDTO(content,filename, contentType)
}

//data class AttachmentDTO (
//        var sentTS: Date?,
//        var content:ByteArray?,
//        var senderId:String,
//        var ticketId:Long?,
//        var filename:String,
//        var contentType:String
//
//)
//
//fun AttachmentDTO.toEntity() : Attachment {
//    val attachment = Attachment()
//    attachment.sentTS = sentTS
//    attachment.content = content
//    attachment.senderId = senderId
//    attachment.ticketId = ticketId
//    attachment.filename = filename
//    attachment.contentType = contentType
//    return attachment
//}
//
//
//
//fun Attachment.toDTO() : AttachmentDTO{
//    return AttachmentDTO(sentTS, content, senderId, ticketId, filename, contentType)
//}