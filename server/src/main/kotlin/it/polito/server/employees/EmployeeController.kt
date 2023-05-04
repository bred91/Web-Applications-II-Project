package it.polito.server.employees

import it.polito.server.tickets.TicketDTO
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
class EmployeeController(private val employeeService: EmployeeService) {

    @PostMapping("/API/employees/")
    @ResponseStatus(HttpStatus.CREATED)
    fun createEmployee(@Valid @RequestBody employee: EmployeeDTO){
        employeeService.createEmployee(employee)
    }

    @GetMapping("/API/employees")
    fun getAllEmployees(): List<EmployeeDTO>{
        return employeeService.getAll()
    }
    @GetMapping("/API/employees/{id}")
    fun getEmployeeById(@PathVariable id: Long) : EmployeeDTO?{
        return employeeService.getEmployee(id)
    }

    @PutMapping("/API/employees/{id}")
    fun updateEmployee(@PathVariable id: Long, @Valid @RequestBody employee: EmployeeDTO) : EmployeeDTO?{
        return employeeService.updateEmployee(id, employee )
    }

    @GetMapping("/API/employees/{id}/tickets")
    fun getTicketsForEmployee(@PathVariable id: Long): List<TicketDTO>{
        return employeeService.getTickets(id)
    }


}