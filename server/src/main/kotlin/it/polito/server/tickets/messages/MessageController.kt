package it.polito.server.tickets.messages

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.multipart.MultipartRequest
import java.util.*
import org.springframework.messaging.simp.SimpMessagingTemplate

@RestController
class MessageController(private val messageService: IMessageService) {

    @Autowired
    private val messagingTemplate: SimpMessagingTemplate? = null


    @GetMapping("/chat/{ticketId}/messages")
    fun getMessages(@PathVariable ticketId: Long) : List<MessageDTO>{
        return messageService.getAllMessages(ticketId)
    }

    @PostMapping("/chat/{ticketId}/messages")
    fun createMessage(@PathVariable ticketId: Long, @RequestPart file: MultipartFile?, @RequestPart text: String?): MessageDTO {
        val message = messageService.createMessage(file, text, ticketId)
        messagingTemplate?.convertAndSendToUser(message.ticketId.toString(), "/messages",message)
        return message
    }

}