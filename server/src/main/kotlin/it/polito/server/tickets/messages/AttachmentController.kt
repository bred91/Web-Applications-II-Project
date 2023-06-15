package it.polito.server.tickets.messages

import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.http.client.MultipartBodyBuilder
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import java.util.Date


@RestController
class AttachmentController(private val attachmentService: AttachmentService) {
    @GetMapping("/chat/{ticketId}/attachments")
    fun getAllAttachments(@PathVariable ticketId:Long) : ResponseEntity<Any> {
        val at = attachmentService.getAllAttachments(ticketId)
        return ResponseEntity.ok()
            .header("Content-Type", "application/pdf")
            .body(at[0].content)
    }


    //@RequestMapping(path = ["/chat/{ticketId}/attachments"], method = , consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    @PostMapping("/chat/{ticketId}/attachments")
    fun createAttachment(@PathVariable ticketId: Long, @RequestPart file: MultipartFile) {
        val attachmentDTO = AttachmentDTO(Date(), file.bytes,"simran", 1, file.name, file.contentType!! )
        return attachmentService.createAttachment(attachmentDTO)
    }

}
