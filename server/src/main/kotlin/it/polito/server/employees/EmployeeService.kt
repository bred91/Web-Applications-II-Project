package it.polito.server.employees

import it.polito.server.employees.exception.EmployeeNotFoundException
import it.polito.server.tickets.ITicketRepository
import it.polito.server.tickets.TicketDTO
import it.polito.server.tickets.toDTO
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Service

@Service
class EmployeeService(
    private val employeeRepository: IEmployeeRepository,
    private val ticketRepository: ITicketRepository
) : IEmployeeService{
    override fun getAll(): List<EmployeeDTO> {
        return employeeRepository.findAll().map { it.toDTO() }
    }

    override fun getEmployee(id: Long): EmployeeDTO? {
        return employeeRepository.findByIdOrNull(id)?.toDTO()
            ?: throw EmployeeNotFoundException("Employee with id $id doesn't exist!")
    }

    override fun createEmployee(employee: EmployeeDTO) {
        employeeRepository.save(employee.toEntity())
    }

    override fun updateEmployee(id: Long, employee: EmployeeDTO): EmployeeDTO? {
        return when (employeeRepository.findByIdOrNull(id)?.toDTO()){
            null -> throw EmployeeNotFoundException("Employee with id $id doesn't exist!")
            else -> employeeRepository.save(employee.toEntity()).toDTO()
        }
    }

    override fun getTickets(id: Long): List<TicketDTO> {
        if(!employeeRepository.existsById(id)){
            throw EmployeeNotFoundException("Employee with id $id doesn't exist!")
        }
        return ticketRepository.findByActualExpertId(id).map { it.toDTO() }
    }

    @PreAuthorize("hasRole('ROLE_Manager')")
    override fun getAllExperts(): List<EmployeeDTO> {
        return employeeRepository.getEmployeesByRole_Id(RoleEnum.EXPERT.toLong()).map { it.toDTO() }
    }
}