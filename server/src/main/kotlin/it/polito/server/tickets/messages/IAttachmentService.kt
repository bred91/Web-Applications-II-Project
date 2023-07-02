package it.polito.server.tickets.messages

import org.bson.types.ObjectId
import org.springframework.web.multipart.MultipartFile

interface IAttachmentService {
    fun getAllAttachments(ticketId: Long): List<AttachmentDTO>
    fun getAttachment(id: ObjectId): AttachmentDTO?
    fun createAttachment(attachment:  AttachmentDTO)
    //fun updateMessage(id: Long, message: MessageDTO): MessageDTO?
}