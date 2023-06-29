package it.polito.server.employees

import it.polito.server.tickets.enums.StateEnum

enum class RoleEnum {
    NULL,
    ADMIN,
    EXPERT,
    MANAGER,
}

fun RoleEnum.toLong(): Long {
    return when (this) {
        RoleEnum.NULL -> 0
        RoleEnum.ADMIN -> 1
        RoleEnum.EXPERT -> 2
        RoleEnum.MANAGER -> 3
    }
}

fun RoleEnum.toWonderfulString(): String {
    return when (this) {
        RoleEnum.NULL -> "NULL"
        RoleEnum.ADMIN -> "ADMIN"
        RoleEnum.EXPERT -> "EXPERT"
        RoleEnum.MANAGER -> "MANAGER"
    }
}