package it.polito.server.tickets.states

interface IStateService {
    fun getAllStates() : List<StateDTO>
    fun createState(state : StateDTO)
    fun updateState(id: Long, state: StateDTO) : StateDTO?
    fun getStateById(id: Long) : StateDTO?
    fun deleteState(id: Long)
}