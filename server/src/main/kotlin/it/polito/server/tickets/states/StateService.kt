package it.polito.server.tickets.states

import it.polito.server.tickets.exception.StateNotFoundException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class StateService(private val stateRepository: IStateRepository) : IStateService {
    override fun getAllStates(): List<StateDTO> {
        return stateRepository.findAll().map { it.toDTO() }
    }


    override fun createState(state: StateDTO) {
        stateRepository.save(state.toEntity())
    }


    override fun updateState(id: Long, state: StateDTO): StateDTO? {
        val oldState = stateRepository.findById(id).orElseThrow {throw StateNotFoundException("State id $id doesn't exist")}
        oldState.name = state.name
        return stateRepository.save(oldState).toDTO()
    }

    override fun getStateById(id: Long): StateDTO? {
        return stateRepository.findByIdOrNull(id)?.toDTO()
            ?: throw StateNotFoundException("State with id $id not found")
    }


    override fun deleteState(id: Long) {
        stateRepository.findById(id).orElseThrow {throw StateNotFoundException("State id $id doesn't exist")}
        stateRepository.deleteById(id)
    }
}