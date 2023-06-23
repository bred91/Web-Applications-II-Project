package it.polito.server.tickets.messages

import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.multipart.MultipartRequest
import java.util.*

@RestController
class MessageController(private val messageService: IMessageService) {

    @GetMapping("/chat/{ticketId}/messages")
    fun getMessages(@PathVariable ticketId: Long) : List<MessageDTO>{
        return messageService.getAllMessages(ticketId)
    }

    @PostMapping("/chat/{ticketId}/messages")
    fun createAttachment(@PathVariable ticketId: Long, @RequestPart file: MultipartFile?, @RequestPart text:String?) {
//        val attachmentDTO = AttachmentDTO(file?.bytes, file?.name, file?.contentType)
//        val contentDTO = ContentDTO(text, attachmentDTO)
//        val messageDTO = MessageDTO(Date(), contentDTO,"simran", 1 )
        return messageService.createMessage(file, text, ticketId)
    }
}