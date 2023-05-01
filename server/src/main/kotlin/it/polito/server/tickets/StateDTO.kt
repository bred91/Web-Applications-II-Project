package it.polito.server.tickets

data class StateDTO(

    val id: Long?,
    val name: String
)

fun StateDTO.toEntity() : State{
    val state = State()
    state.id = id
    state.name = name

    return state;
}

fun State.toDTO() : StateDTO {
    return StateDTO(id, name)
}