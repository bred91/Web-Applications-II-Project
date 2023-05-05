package it.polito.server.products.exception

import it.polito.server.Exception.DuplicateException
import it.polito.server.Exception.NotFoundException
import it.polito.server.Exception.Exception

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

class ProductNotFoundException(message:String): NotFoundException(message)
class ProductDuplicateException(message: String): DuplicateException(message)
class PurchaseNotAssociatedException(message: String): Exception(message)

class PurchaseNotFoundException(message: String) : NotFoundException(message)

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