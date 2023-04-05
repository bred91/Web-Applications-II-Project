package it.polito.server.products.exception

import it.polito.server.DuplicateException
import it.polito.server.NotFoundException
import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

/*
@RestControllerAdvice
class ProductExceptionHandlerController {
    @ExceptionHandler(MethodArgumentNotValidException::class)
    @ResponseBody
    fun handleMethodArgumentNotValidException(ex: MethodArgumentNotValidException): ResponseEntity<ProblemDetail> {
        val errorMessage =
            ex.bindingResult.fieldErrors.joinToString(separator = "\n\t") { "${it.field}: ${it.defaultMessage}" }
        return ResponseEntity.badRequest().body(ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "Validation failed:\n\t$errorMessage"))
    }
}
*/

class ProductNotFoundException(message:String):NotFoundException(message)
class ProductDuplicateException(message: String):DuplicateException(message)

/*
@RestControllerAdvice
class ProductProblemDetailsHandler:ResponseEntityExceptionHandler() {

    @ExceptionHandler(ProductNotFoundException::class)
    fun handleProfileNotFound(e: ProductNotFoundException) = ProblemDetail
        .forStatusAndDetail(HttpStatus.NOT_FOUND, e.message!!)

    @ExceptionHandler(ProductDuplicateException::class)
    fun handleDuplicateProfile(e:ProductDuplicateException) = ProblemDetail
        .forStatusAndDetail(HttpStatus.CONFLICT, e.message!!)
}
*/