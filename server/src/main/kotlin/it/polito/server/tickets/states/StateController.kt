package it.polito.server.tickets.states

import org.springframework.web.bind.annotation.*

@RestController
class StateController(private val stateService: IStateService) {

    @GetMapping("/API/state")
    fun getAll() : List<StateDTO> {
        return stateService.getAllStates()
    }

    @GetMapping("/API/state/{id}")
    fun getStateById(@PathVariable id: Long): StateDTO? {
        return stateService.getStateById(id)
    }

    @PostMapping("/API/state")
    fun createState(state: StateDTO){
        return stateService.createState(state)
    }

    @DeleteMapping("/API/state/{id}")
    fun deleteState(@PathVariable id: Long){
        return stateService.deleteState(id)
    }
}