package it.polito.server.tickets.attachments

import it.polito.server.tickets.attachments.AttachmentDTO

interface IAttachmentService {
        fun getAllAttachments(): List<AttachmentDTO>
        fun getAttachmentById(id: Long): AttachmentDTO?
        fun createAttachment(attachment: AttachmentDTO)
        fun updateAttachment(id: Long, attachment: AttachmentDTO): AttachmentDTO?
}
