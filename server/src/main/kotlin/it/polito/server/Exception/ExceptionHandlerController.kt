package it.polito.server.Exception

import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

@RestControllerAdvice
class ExceptionHandlerController {
    @ExceptionHandler(MethodArgumentNotValidException::class)
    @ResponseBody
    fun handleMethodArgumentNotValidException(ex: MethodArgumentNotValidException): ResponseEntity<ProblemDetail> {
        val errorMessage =
            ex.bindingResult.fieldErrors.joinToString(separator = "\n\t") { "${it.field}: ${it.defaultMessage}" }
        return ResponseEntity.badRequest().body(ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "Validation failed:\n\t$errorMessage"))
    }
}

open class NotFoundException(message:String):RuntimeException(message)
open class DuplicateException(message: String):RuntimeException(message)
open class Exception(message: String) : RuntimeException(message)

open class IllegalStateException(message: String) : RuntimeException(message)

open class AuthorizationServiceException(message: String) : RuntimeException(message)

@RestControllerAdvice
class ProblemDetailsHandler: ResponseEntityExceptionHandler() {

    @ExceptionHandler(NotFoundException::class)
    fun handleNotFound(e: NotFoundException) = ProblemDetail
        .forStatusAndDetail(HttpStatus.NOT_FOUND, e.message!!)

    @ExceptionHandler(DuplicateException::class)
    fun handleDuplicate(e: DuplicateException) = ProblemDetail
        .forStatusAndDetail(HttpStatus.CONFLICT, e.message!!)

    @ExceptionHandler(Exception::class)
    fun handleException(e: Exception) = ProblemDetail
        .forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, e.message!!)

    @ExceptionHandler(IllegalStateException::class)
    fun handleIllegalStateException(e: IllegalStateException) = ProblemDetail
        .forStatusAndDetail(HttpStatus.CONFLICT, e.message!!)

    @ExceptionHandler(AuthorizationServiceException::class)
    fun handleAuthorizationServiceException(e: AuthorizationServiceException) = ProblemDetail
        .forStatusAndDetail(HttpStatus.FORBIDDEN, e.message!!)

}