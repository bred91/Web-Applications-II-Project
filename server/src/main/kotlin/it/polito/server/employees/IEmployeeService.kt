package it.polito.server.employees

interface IEmployeeService {
    fun getAll(): List<EmployeeDTO>
    fun getEmployee(id: Int): EmployeeDTO?
    fun createEmployee(employee: EmployeeDTO)
    fun updateEmployee(id: Int, employee: EmployeeDTO): EmployeeDTO?
}