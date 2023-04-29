package it.polito.server.tickets

import java.util.*

data class MessageDTO (
    val id: Long? = null,
    val sentTS: Date?,
    val text: String,
    val isSenderCustomer: Boolean,
    /*val expert: EmployeeDTO?,
    val ticket: TicketDTO?,*/
    val attachments: List<AttachmentDTO> = listOf(),
)

/*
fun MessageDTO.toEntity(): Message{
    val message = Message()
    message.id = id
    message.sentTS = sentTS
    message.text = text
    message.isSenderCustomer = isSenderCustomer
    message.expert = expert?.toEntity()
    message.ticket = ticket?.toEntity()
    message.attachments = attachments.map { it.toEntity() }
    return message
}

fun Message.toDTO() = MessageDTO(id, sentTS, text, isSenderCustomer, expert?.toDTO(), ticket?.toDTO(), attachments?.map { it.toDTO() } ?: listOf())

*/