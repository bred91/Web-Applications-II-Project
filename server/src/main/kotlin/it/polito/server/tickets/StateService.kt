package it.polito.server.tickets

import it.polito.server.Exception.NotFoundException
import it.polito.server.profiles.toDTO
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class StateService(private val stateRepository: IStateRepository) : IStateService {
    override fun getAll(): List<StateDTO> {
        return stateRepository.findAll().map { it.toDTO() }
    }

    override fun createState(state: StateDTO) {
        stateRepository.save(state.toEntity())
    }

    override fun updateState(id: Long, state: StateDTO): StateDTO? {
        var old_state = stateRepository.findById(id).orElseThrow {throw NotFoundException("State id $id doesn't exist")}
        old_state.name = state.name
        return stateRepository.save(old_state).toDTO()
    }

    override fun getState(id: Long): StateDTO? {
        return stateRepository.findByIdOrNull(id)?.toDTO()
            ?: throw NotFoundException("State with id $id not found")
    }

    override fun deleteState(id: Long) {
        stateRepository.findById(id).orElseThrow {throw NotFoundException("State id $id doesn't exist")}
        stateRepository.deleteById(id)
    }


}