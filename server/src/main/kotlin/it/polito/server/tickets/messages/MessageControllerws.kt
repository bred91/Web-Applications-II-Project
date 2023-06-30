package it.polito.server.tickets.messages

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.messaging.handler.annotation.SendTo
import org.springframework.web.bind.annotation.RestController

//
//import org.springframework.beans.factory.annotation.Autowired
//import org.springframework.messaging.handler.annotation.MessageMapping
//import org.springframework.messaging.handler.annotation.Payload
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.web.multipart.MultipartFile

//import org.springframework.web.bind.annotation.*
//import org.springframework.web.multipart.MultipartFile

data class MessageRec (
    var file: MultipartFile?,
    var text: String?,
    var ticketId: Long
)

@RestController
class MessageControllerws(private val messageService: IMessageService) {
    @Autowired
    private val messagingTemplate: SimpMessagingTemplate? = null



    /*@GetMapping("/chat/{ticketId}/messages")
    fun getMessages(@PathVariable ticketId: Long) : List<MessageDTO>{
        return messageService.getAllMessages(ticketId)
    }*/

//    @MessageMapping("/message")
//    @SendTo("/chatroom/public")
//    fun receiveMessage(@Payload message:MessageRec){
//        return message;
//    }

    @MessageMapping("/private-message")
    fun recMessage(@Payload message:MessageRec) {
        println(message)
        val saved = messageService.createMessage(message.file, message.text, message.ticketId)
        messagingTemplate?.convertAndSendToUser(message.ticketId.toString(), "/private",message)


    }

    /*@MessageMapping("/chat")
    fun processMessage(@Payload chatMessage: MessageDTO) {

        val saved: MessageDTO = messageService.createMessage(chatMessage)
        messagingTemplate!!.convertAndSendToUser(
            chatMessage.getRecipientId(), "/queue/messages",
            ChatNotification(
                saved.getId(),
                saved.getSenderId(),
                saved.getSenderName()
//            )
        )
    }*/

    //@PostMapping("/chat/{ticketId}/messages")
//    fun createAttachment(@PathVariable ticketId: Long, @RequestPart file: MultipartFile?, @RequestPart text:String?) {
//
//          messageService.createMessage(file, text, ticketId)
//    }
}