package it.polito.server.profiles.exception

import it.polito.server.Exception.DuplicateException
import it.polito.server.Exception.NotFoundException



class ProfileNotFoundException(message:String): NotFoundException(message)
class DuplicateProfileException(message: String): DuplicateException(message)


