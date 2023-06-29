package it.polito.server.tickets.enums

import java.util.*

enum class TimeEnum {
    LAST_24H,
    LAST_WEEK,
    LAST_MONTH,
    LAST_QUARTER,
    LAST_SEMESTER,
    LAST_YEAR
}

fun TimeEnum.toDate(): Date {
    return when (this) {
        TimeEnum.LAST_24H -> Date(Date().time - 1L * 24L * 60L * 60L * 1000L)
        TimeEnum.LAST_WEEK -> Date(Date().time - 7L * 24L * 60L * 60L * 1000L)
        TimeEnum.LAST_MONTH -> Date(Date().time - 30L * 24L * 60L * 60L * 1000L)
        TimeEnum.LAST_QUARTER -> Date(Date().time - 91L * 24L * 60L * 60L * 1000L)
        TimeEnum.LAST_SEMESTER -> Date(Date().time - 182L * 24L * 60L * 60L * 1000L)
        TimeEnum.LAST_YEAR -> Date(Date().time - 365L * 24L * 60L * 60L * 1000L)
    }
}