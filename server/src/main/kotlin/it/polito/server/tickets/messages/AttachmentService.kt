package it.polito.server.tickets.messages

import org.bson.types.ObjectId
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile

//@Service
//class AttachmentService(private val attachmentRepository: IAttachmentRepository) : IAttachmentService{
//    override fun getAllAttachments(ticketId: Long): List<AttachmentDTO> {
//        return attachmentRepository.getAllAttachmentsByTicketId(ticketId).map { it.toDTO() }
//    }
//
//    override fun getAttachment(id: ObjectId): AttachmentDTO? {
//        TODO("Not yet implemented")
//    }
//
//    override fun createAttachment(attachment: AttachmentDTO) {
//        attachmentRepository.save(attachment.toEntity())
//    }
//
//}