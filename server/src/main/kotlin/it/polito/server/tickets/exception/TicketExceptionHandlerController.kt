package it.polito.server.tickets.exception


import it.polito.server.Exception.NotFoundException
import it.polito.server.Exception.IllegalStateException
import it.polito.server.Exception.AuthorizationServiceException

class TicketNotFoundException(message:String): NotFoundException(message)

class StateNotFoundException(message:String): NotFoundException(message)
class PriorityNotFoundException(message:String): NotFoundException(message)
class StateNotValidException(message:String): IllegalStateException(message)

class AuthorizationServiceException(message: String) : AuthorizationServiceException(message)