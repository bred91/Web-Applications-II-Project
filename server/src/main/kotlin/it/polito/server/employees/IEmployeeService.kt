package it.polito.server.employees

import it.polito.server.tickets.TicketDTO

interface IEmployeeService {
    fun getAll(): List<EmployeeDTO>
    fun getEmployee(id: Long): EmployeeDTO?
    fun createEmployee(employee: EmployeeDTO)
    fun updateEmployee(id: Long, employee: EmployeeDTO): EmployeeDTO?

    fun getTickets(id: Long) : List<TicketDTO>
    fun getAllExperts(): List<EmployeeDTO>
}