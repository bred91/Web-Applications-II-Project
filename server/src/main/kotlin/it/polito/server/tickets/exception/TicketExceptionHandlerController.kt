package it.polito.server.tickets.exception

import it.polito.server.Exception.DuplicateException
import it.polito.server.Exception.NotFoundException
import it.polito.server.Exception.Exception
import it.polito.server.Exception.IllegalStateException

class TicketNotFoundException(message:String): NotFoundException(message)
class DuplicateTicketException(message: String): DuplicateException(message)

class StateNotFoundException(message:String): NotFoundException(message)
class PriorityNotFoundException(message:String): NotFoundException(message)
class StateNotValidException(message:String): IllegalStateException(message)
class NullTicketIdException(message: String): Exception(message)
