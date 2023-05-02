package it.polito.server.tickets.enums

enum class StateEnum {
    NULL,
    OPEN,
    IN_PROGRESS,
    RESOLVED,
    CLOSED,
    REOPENED
}

fun StateEnum.toLong(): Long {
    return when (this) {
        StateEnum.NULL -> 0
        StateEnum.OPEN -> 1
        StateEnum.IN_PROGRESS -> 2
        StateEnum.RESOLVED -> 3
        StateEnum.CLOSED -> 4
        StateEnum.REOPENED -> 5
    }
}

fun StateEnum.toWonderfulString(): String {
    return when (this) {
        StateEnum.NULL -> "NULL"
        StateEnum.OPEN -> "OPEN"
        StateEnum.IN_PROGRESS -> "IN PROGRESS"
        StateEnum.RESOLVED -> "RESOLVED"
        StateEnum.CLOSED -> "CLOSED"
        StateEnum.REOPENED -> "REOPENED"
    }
}