package it.polito.server.tickets

data class PriorityDTO (
    val id: Long,
    val name: String
    )

fun PriorityDTO.toEntity() : Priority{
    var priority = Priority()
    priority.id = id
    priority.name = name

    return priority;
}

fun Priority.toDTO() : PriorityDTO {
    return PriorityDTO(id, name)
}
