package it.polito.server.tickets.messages

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class MessageController(private val messageService: IMessageService) {

    @GetMapping("/chat/{ticketId}/messages")
    fun getMessages(@PathVariable ticketId: Long) : List<MessageDTO>{
        return messageService.getAllMessages(ticketId)
    }

    @PostMapping("/chat/{ticketId}/messages")
    fun postMessage(@RequestBody message: Message) {
        messageService.createMessage(message.toDTO())
    }
}