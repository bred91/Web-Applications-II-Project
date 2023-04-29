package it.polito.server.tickets

interface IAttachmentService {
        fun getAll(): List<AttachmentDTO>
        fun getAttachment(id: Long): AttachmentDTO?
        fun createAttachment(attachment: AttachmentDTO)
        fun updateAttachment(id: Long, attachment: AttachmentDTO): AttachmentDTO?
}
