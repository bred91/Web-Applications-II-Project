package it.polito.server.tickets

import it.polito.server.Exception.NotFoundException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class StateService(private val stateRepository: IStateRepository) : IStateService {
    override fun getAllStates(): List<StateDTO> {
        return stateRepository.findAll().map { it.toDTO() }
    }

    // todo: @PreAuthorize("hasRole('ROLE_ADMIN')")
    override fun createState(state: StateDTO) {
        stateRepository.save(state.toEntity())
    }

    // todo: @PreAuthorize("hasRole('ROLE_ADMIN')")
    override fun updateState(id: Long, state: StateDTO): StateDTO? {
        val oldState = stateRepository.findById(id).orElseThrow {throw NotFoundException("State id $id doesn't exist")}
        oldState.name = state.name
        return stateRepository.save(oldState).toDTO()
    }

    override fun getState(id: Long): StateDTO? {
        return stateRepository.findByIdOrNull(id)?.toDTO()
            ?: throw NotFoundException("State with id $id not found")
    }

    // todo: @PreAuthorize("hasRole('ROLE_ADMIN')")
    override fun deleteState(id: Long) {
        stateRepository.findById(id).orElseThrow {throw NotFoundException("State id $id doesn't exist")}
        stateRepository.deleteById(id)
    }
}