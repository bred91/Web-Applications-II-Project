package it.polito.server.profiles.exception

import it.polito.server.Exceptions.DuplicateException
import it.polito.server.Exceptions.NotFoundException

/*
@RestControllerAdvice
class ProfileExceptionHandlerController {
    @ExceptionHandler(MethodArgumentNotValidException::class)
    @ResponseBody
    fun handleMethodArgumentNotValidException(ex: MethodArgumentNotValidException): ResponseEntity<ProblemDetail> {
        val errorMessage =
            ex.bindingResult.fieldErrors.joinToString(separator = "\n\t") { "${it.field}: ${it.defaultMessage}" }
        return ResponseEntity.badRequest().body(ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "Validation failed:\n\t$errorMessage"))
    }
}
*/

class ProfileNotFoundException(message:String): NotFoundException(message)
class DuplicateProfileException(message: String): DuplicateException(message)

/*
@RestControllerAdvice
class ProblemDetailsHandler:ResponseEntityExceptionHandler() {

    @ExceptionHandler(ProfileNotFoundException::class)
    fun handleProfileNotFound(e: ProfileNotFoundException) = ProblemDetail
        .forStatusAndDetail(HttpStatus.NOT_FOUND, e.message!!)

    @ExceptionHandler(DuplicateProfileException::class)
    fun handleDuplicateProfile(e:DuplicateProfileException) = ProblemDetail
        .forStatusAndDetail(HttpStatus.CONFLICT, e.message!!)

}
*/
