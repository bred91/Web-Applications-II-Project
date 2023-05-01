package it.polito.server.tickets

import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class StateController(private val stateService: IStateService) {

    @GetMapping("/API/state")
    fun getAll() : List<StateDTO> {
        return stateService.getAll()
    }

    @GetMapping("/API/state/{id}")
    fun getStateById(@PathVariable id: Long): StateDTO? {
        return stateService.getState(id)
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