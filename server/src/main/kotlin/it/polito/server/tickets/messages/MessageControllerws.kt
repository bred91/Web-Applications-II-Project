//package it.polito.server.tickets.messages
//
//import org.springframework.beans.factory.annotation.Autowired
//import org.springframework.messaging.handler.annotation.MessageMapping
//import org.springframework.messaging.handler.annotation.Payload
//import org.springframework.messaging.simp.SimpMessagingTemplate
//import org.springframework.web.bind.annotation.*
//import org.springframework.web.multipart.MultipartFile
//import java.util.*
//
//
//
//@RestController
//class MessageControllerws(private val messageService: IMessageService) {
//    @Autowired
//    private val messagingTemplate: SimpMessagingTemplate? = null
//
//    @Autowired
//    private val chatMessageService: MessageService? = null
//
//    /*@GetMapping("/chat/{ticketId}/messages")
//    fun getMessages(@PathVariable ticketId: Long) : List<MessageDTO>{
//        return messageService.getAllMessages(ticketId)
//    }*/
//
//    /*@MessageMapping("/chat")
//    fun processMessage(@Payload chatMessage: MessageDTO) {
//
//        val saved: MessageDTO = messageService.createMessage(chatMessage)
//        messagingTemplate!!.convertAndSendToUser(
//            chatMessage.getRecipientId(), "/queue/messages",
//            ChatNotification(
//                saved.getId(),
//                saved.getSenderId(),
//                saved.getSenderName()
////            )
//        )
//    }*/
//
//    //@PostMapping("/chat/{ticketId}/messages")
//    fun createAttachment(@PathVariable ticketId: Long, @RequestPart file: MultipartFile?, @RequestPart text:String?) {
////        val attachmentDTO = AttachmentDTO(file?.bytes, file?.name, file?.contentType)
////        val contentDTO = ContentDTO(text, attachmentDTO)
////        val messageDTO = MessageDTO(Date(), contentDTO,"simran", 1 )
//          messageService.createMessage(file, text, ticketId)
//         // messagingTemplate.convertAndSendToUser()
//    }
//}