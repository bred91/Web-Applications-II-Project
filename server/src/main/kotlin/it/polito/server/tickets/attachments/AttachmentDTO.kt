//package it.polito.server.tickets.attachments
//
//class AttachmentDTO (
//    val id: Long? = null,
//    val name: String = "",
//    val type: String = "",
//    val binaryString: String = "",
//    //val message: MessageDTO? = null,
//)
//
//fun AttachmentDTO.toEntity(): Attachment {
//    val attachment = Attachment()
//    attachment.id = id
//    attachment.name = name
//    attachment.type = type
//    attachment.binaryString = binaryString
//    //attachment.message = message?.toEntity()
//    return attachment
//}
//
//fun Attachment.toDTO() = AttachmentDTO(id, name, type, binaryString)
