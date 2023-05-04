package it.polito.server.employees.exception

import it.polito.server.Exception.DuplicateException
import it.polito.server.Exception.NotFoundException


class EmployeeNotFoundException(message:String): NotFoundException(message)
