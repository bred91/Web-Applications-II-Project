package it.polito.server.tickets.messages

import it.polito.server.employees.EmployeeDTO
import it.polito.server.employees.toDTO
import it.polito.server.employees.toEntity
import org.bson.types.ObjectId
import java.util.*


//data class MessageDTO (
//    //val id: Long? = null,
//    //val id: ObjectId? = null,
//    val sentTS: Date?,
//    val content: String,
//    val senderId: String,
//    val ticketId: Long?
//)

data class MessageDTO (
    //val id: ObjectId? = null,
    val sentTS: Date?,
    val content: ContentDTO,
    val senderId: String,
    val ticketId: Long?
)


data class ContentDTO(
    val text:String?,
    val attachment:AttachmentDTO?
)

fun MessageDTO.toEntity(): Message {
    val message = Message()
    val contentEntity = Content()
    //val attachmentEntity = content.attachment?.toEntity()
    //attachmentEntity.content = content.attachment?.content
    //attachmentEntity.contentType = content.attachment?.contentType
    //attachmentEntity.filename = content.attachment?.filename
    contentEntity.text = content?.text
    contentEntity.attachment = content.attachment?.toEntity()
    message.sentTS = sentTS
    message.content = contentEntity
    message.ticketId = ticketId
    message.senderId = senderId
    return message
}

fun Message.toDTO() : MessageDTO {
    if(content?.attachment?.content == null) {
        content?.attachment = null
    }
    val contentDTO = ContentDTO(content?.text, content?.attachment?.toDTO())
    return MessageDTO(sentTS, contentDTO, senderId, ticketId )
}




//fun MessageDTO.toEntity(): Message {
//    val message = Message()
//    //message.id = id
//    message.sentTS = sentTS
//    message.content = content
//    message.ticketId = ticketId
//    message.senderId = senderId
//    return message
//}
//
//fun Message.toDTO() : MessageDTO {
//    return MessageDTO(sentTS, content, senderId, ticketId )
//}

//data class MessageDTO (
//    val id: Long? = null,
//    val sentTS: Date?,
//    val text: String,
//    val isSenderCustomer: Boolean,
//    val expert: EmployeeDTO?,
//    /*val ticket: TicketDTO?,*/
//    val attachments: List<AttachmentDTO> = listOf(),
//)
//
//fun MessageDTO.toEntity(): Message {
//    val message = Message()
//    message.id = id
//    message.sentTS = sentTS
//    message.text = text
//    message.isSenderCustomer = isSenderCustomer
//    message.expert = expert?.toEntity()
//    /*message.ticket = ticket?.toEntity()*/
//    message.attachments = attachments.map { it.toEntity() }
//    return message
//}
//
//fun Message.toDTO() : MessageDTO {
//    return MessageDTO(id, sentTS, text, isSenderCustomer, expert?.toDTO(), attachments?.map { it.toDTO() } ?: listOf())
//}

