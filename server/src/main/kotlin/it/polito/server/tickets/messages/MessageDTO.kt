package it.polito.server.tickets.messages

import java.util.*



data class MessageDTO (

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

