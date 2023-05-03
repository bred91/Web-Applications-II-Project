package it.polito.server.tickets

interface IAttachmentService {
        fun getAllAttachments(): List<AttachmentDTO>
        fun getAttachmentById(id: Long): AttachmentDTO?
        fun createAttachment(attachment: AttachmentDTO)
        fun updateAttachment(id: Long, attachment: AttachmentDTO): AttachmentDTO?
}
