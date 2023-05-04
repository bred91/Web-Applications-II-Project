package it.polito.server.tickets.enums

enum class PriorityLevelEnum {
    LOW,
    MEDIUM,
    HIGH
}
fun PriorityLevelEnum.toLong(): Long {
    return when (this) {
        PriorityLevelEnum.LOW -> 0
        PriorityLevelEnum.MEDIUM -> 1
        PriorityLevelEnum.HIGH -> 2
    }
}

fun PriorityLevelEnum.toString() : String {
        return when (this){
                PriorityLevelEnum.HIGH -> "HIGH"
                PriorityLevelEnum.MEDIUM -> "MEDIUM"
                PriorityLevelEnum.LOW -> "LOW"
        }
}

