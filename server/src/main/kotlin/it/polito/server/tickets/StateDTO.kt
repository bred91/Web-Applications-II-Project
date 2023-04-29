package it.polito.server.tickets

data class StateDTO(

    var id: Long?,
    var name: String
)

fun StateDTO.toEntity() : State{
    var state = State()
    state.id = id
    state.name = name

    return state;
}