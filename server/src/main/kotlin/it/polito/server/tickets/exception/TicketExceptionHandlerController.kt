package it.polito.server.tickets.exception

import it.polito.server.Exception.DuplicateException
import it.polito.server.Exception.NotFoundException


class TicketNotFoundException(message:String): NotFoundException(message)
class DuplicateTicketException(message: String): DuplicateException(message)

class StateNotFoundException(message:String): NotFoundException(message)

class NullTicketIdException(message: String): Exception(message)
