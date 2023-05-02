package it.polito.server.tickets

interface IStateService {
    fun getAllStates() : List<StateDTO>
    fun createState(state : StateDTO)
    fun updateState(id: Long, state: StateDTO) : StateDTO?
    fun getState(id: Long) : StateDTO?
    fun deleteState(id: Long)
}