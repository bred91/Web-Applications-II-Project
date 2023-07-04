package it.polito.server.tickets.messages



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

