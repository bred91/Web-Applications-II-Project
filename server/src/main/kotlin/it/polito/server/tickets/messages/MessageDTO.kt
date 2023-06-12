package it.polito.server.tickets.messages

import it.polito.server.employees.EmployeeDTO
import it.polito.server.employees.toDTO
import it.polito.server.employees.toEntity
import java.util.*


data class MessageDTO (
    val id: Long? = null,
    val sentTS: Date?,
    val content: String,
    val senderId: String,
    val ticketId: Long?
)

fun MessageDTO.toEntity(): Message {
    val message = Message()
    message.id = id
    message.sentTS = sentTS
    message.content = content
    message.ticketId = ticketId
    message.senderId = senderId
    return message
}

fun Message.toDTO() : MessageDTO {
    return MessageDTO(id, sentTS, content, senderId, ticketId )
}

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

